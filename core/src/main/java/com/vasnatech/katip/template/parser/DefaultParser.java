package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.expression.Expression;
import com.vasnatech.commons.expression.ExpressionException;
import com.vasnatech.commons.expression.parser.DefaultExpressionParser;
import com.vasnatech.commons.expression.parser.ExpressionParser;
import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.renderer.TagRenderer;
import com.vasnatech.katip.template.renderer.TagRenderers;
import com.vasnatech.katip.template.document.Text;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultParser implements Parser {

    enum TagTokenType {Open, Close, EndOpen, EndClose, LineComment, EndOfLine}

    enum TagAttributeTokenType {WhiteSpace, Equal, DoubleQuotes}

    final Tokenizer<TagTokenType> tagTokenizer;
    final Tokenizer<TagAttributeTokenType> tagAttributeTokenizer;
    final ExpressionParser expressionParser;

    DefaultParser() {
        tagTokenizer = new Tokenizer<>(
                new Token<>("<<--", TagTokenType.Open),
                new Token<>("-->>", TagTokenType.Close),
                new Token<>("<<==", TagTokenType.EndOpen),
                new Token<>("==>>", TagTokenType.EndClose),
                new Token<>("<<//", TagTokenType.LineComment),
                new Token<>("\r\n", TagTokenType.EndOfLine),
                new Token<>("\n", TagTokenType.EndOfLine)
        );
        tagAttributeTokenizer = new Tokenizer<>(
                new Token<>("=", TagAttributeTokenType.Equal),
                new Token<>("\"", TagAttributeTokenType.DoubleQuotes),
                new Token<>(" ", TagAttributeTokenType.WhiteSpace, false),
                new Token<>("\t", TagAttributeTokenType.WhiteSpace, false),
                new Token<>("\b", TagAttributeTokenType.WhiteSpace, false)
        );
        expressionParser = new DefaultExpressionParser();
    }

    @Override
    public Document parse(CharSequence source) {
        final Iterator<Token<TagTokenType>> iterator = tagTokenizer.tokenize(source);
        final Deque<Tag> containers = new LinkedList<>();
        containers.offer(new Tag(0, TagRenderers.get("root")));
        Token<TagTokenType> previousToken = null;
        Token<TagTokenType> currentToken = null;
        AtomicInteger line = new AtomicInteger(1);
        int state = 0; // 0: start  10: tag-open  11: tag-content  12: tag-close  20: comment  30: end-of-line 40: text
        boolean goNext = true;
        while (iterator.hasNext() || !goNext) {
            if (goNext) {
                previousToken = currentToken;
                currentToken = iterator.next();
            }
            goNext = true;
            switch (state) {
                case 0 -> {
                    if (currentToken.getValue() == TagTokenType.Open) {
                        state = 10;
                    } else if (currentToken.getValue() == TagTokenType.EndOpen) {
                        state = 15;
                    } else if (currentToken.getValue() == TagTokenType.LineComment) {
                        goNext = false;
                        state = 20;
                    } else if (currentToken.getValue() == TagTokenType.EndOfLine) {
                        goNext = false;
                        state = 30;
                    } else if (currentToken.getValue() == null) {
                        goNext = false;
                        state = 40;
                    } else {
                        throw new RuntimeException("Invalid tag at line " + line + ". Not expecting " + currentToken.getMatch());
                    }
                }
                case 10 -> {
                    if (currentToken.getValue() != null) {
                        throw new RuntimeException("Invalid tag at line " + line + ". Expecting tag content but found " + currentToken.getMatch());
                    }
                    state = 11;
                }
                case 11 -> {
                    if (currentToken.getValue() != TagTokenType.Close) {
                        throw new RuntimeException("Invalid tag at line " + line + ". Expecting close token.");
                    }
                    String tagText = previousToken.getMatch().trim();
                    Tag tag = parseTag(tagText, line);
                    containers.peek().addChild(tag);
                    if (tag.isContainer()) {
                        containers.push(tag);
                    }
                    state = 0;
                }
                case 15 -> {
                    if (currentToken.getValue() != null) {
                        throw new RuntimeException("Invalid tag at line " + line + ". Expecting end of tag but found " + currentToken.getMatch());
                    }
                    state = 16;
                }
                case 16 -> {
                    if (currentToken.getValue() != TagTokenType.EndClose) {
                        throw new RuntimeException("Invalid tag at line " + line + ". Expecting end close token.");
                    }
                    String tagText = previousToken.getMatch().trim();
                    Tag container = containers.pop();
                    if (!tagText.equals(container.name())) {
                        throw new RuntimeException("Invalid tag end at line " + line + ". Expecting end of tag " + container.name() + " but found " + tagText);
                    }
                    if (containers.isEmpty()) {
                        throw new RuntimeException("Unexpected end tag at line " + line + ".");
                    }
                    state = 0;
                }
                case 20 -> {
                    //TODO remove while we are already iterating
                    //TODO what happens if end of line does not exists?
                    while (iterator.hasNext()) {
                        if (iterator.next().getValue() == TagTokenType.EndOfLine) {
                            line.incrementAndGet();
                            break;
                        }
                    }
                    state = 0;
                }
                case 30 -> {
                    if (previousToken == null || previousToken.getValue() == null || previousToken.getValue() == TagTokenType.EndOfLine) {
                        Text text = new Text(currentToken.getMatch());
                        containers.peek().addChild(text);
                    }
                    line.incrementAndGet();
                    state = 0;
                }
                case 40 -> {
                    Text text = new Text(currentToken.getMatch());
                    containers.peek().addChild(text);
                    state = 0;
                }
            }
        }
        if (state != 0) {
            throw new RuntimeException("Unexpected end of document. State: " + state + ".");
        }

        return new Document(containers.poll());
    }

    Tag parseTag(String tagText, AtomicInteger line) {
        int index = tagText.indexOf(' ');
        if (index < 0) {
            throw new RuntimeException("Unexpected tag " + tagText + " at line " + line + ".");
        }
        String tagName = tagText.substring(0, index);
        String tagAttributesAsText = tagText.substring(index + 1);

        TagRenderer renderer = TagRenderers.get(tagName);
        if (renderer == null) {
            throw new RuntimeException("Unknown tag renderer " + tagName + " at line " + line + ".");
        }
        Tag tag = new Tag(line.get(), renderer);
        parseTagAttributes(tag, tagAttributesAsText, line);
        tag.validate();
        return tag;
    }

    void parseTagAttributes(Tag tag, String tagAttributesAsText, AtomicInteger line) {
        Iterator<Token<TagAttributeTokenType>> iterator = tagAttributeTokenizer.tokenize(tagAttributesAsText);
        String currentAttrName = null;
        String currentExpression = null;
        int state = 0; // 0: start  1: attribute name  2: equals  3: double quotes  4: expression  5: double quotes
        while (iterator.hasNext()) {
            Token<TagAttributeTokenType> token = iterator.next();
            switch (state) {
                case 0 -> {
                    if (token.getValue() != null || !isJavaIdentifier(token.getMatch())) {
                        throw new RuntimeException("Expecting attribute name but found '" + token.getMatch() + "' at line " + line + ".");
                    }
                    currentAttrName = token.getMatch();
                    state = 1;
                }
                case 1 -> {
                    if (token.getValue() != TagAttributeTokenType.Equal) {
                        throw new RuntimeException("Expecting '=' but found '" + token.getMatch() + "' at line " + line + ".");
                    }
                    state = 2;
                }
                case 2 -> {
                    if (token.getValue() != TagAttributeTokenType.DoubleQuotes) {
                        throw new RuntimeException("Expecting '\"' but found '" + token.getMatch() + "' at line " + line + ".");
                    }
                    state = 3;
                }
                case 3 -> {
                    if (token.getValue() != null) {
                        throw new RuntimeException("Expecting an expression but found '" + token.getMatch() + "' at line " + line + ".");
                    }
                    currentExpression = token.getMatch();
                    state = 4;
                }
                case 4 -> {
                    if (token.getValue() != TagAttributeTokenType.DoubleQuotes) {
                        throw new RuntimeException("Expecting '\"' but found '" + token.getMatch() + "' at line " + line + ".");
                    }
                    state = 5;
                }
            }
            if (state == 5) {
                Expression expression = parseExpression(currentExpression, line);
                tag.addAttribute(currentAttrName, expression);
                currentAttrName = null;
                currentExpression = null;
                state = 0;
            }
        }
        if (state != 0) {
            throw new RuntimeException("Incomplete attribute at line " + line + ".");
        }
    }

    Expression parseExpression(String expressionAsText, AtomicInteger line) {
        try {
            return expressionParser.parse(expressionAsText);
        } catch (ExpressionException e) {
            throw new RuntimeException("Unable to parse expression" + expressionAsText + " at lime " + line + ". " + e.getMessage(), e);
        }
    }

    static boolean isJavaIdentifier(CharSequence identifier) {
        int length = identifier.length();
        if (length == 0) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(identifier.charAt(0))) {
            return false;
        }
        for (int index = 1; index < length; ++index) {
            if (!Character.isJavaIdentifierPart(identifier.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    static Long parseLong(CharSequence text) {
        try {
            return Long.parseLong(text, 0, text.length(), 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Boolean parseBoolean(String text) {
        if ("false".equals(text)) {
            return Boolean.FALSE;
        }
        if ("true".equals(text)) {
            return Boolean.TRUE;
        }
        return null;
    }
}
