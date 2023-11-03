package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.renderer.TagRenderer;
import com.vasnatech.katip.template.renderer.TagRenderers;
import com.vasnatech.katip.template.document.Text;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

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
                new Token<>(" ", TagAttributeTokenType.WhiteSpace),
                new Token<>("\t", TagAttributeTokenType.WhiteSpace),
                new Token<>("\b", TagAttributeTokenType.WhiteSpace, false)
        );
        expressionParser = new SpelExpressionParser();
    }

    @Override
    public Document parse(ParseContext parseContext) {
        final Iterator<Token<TagTokenType>> iterator = tagTokenizer.tokenize(parseContext.getContent());
        final Deque<Tag> containers = new LinkedList<>();
        containers.offer(new Tag(parseContext.getPath(), 0, TagRenderers.get("root")));
        Token<TagTokenType> previousToken = null;
        Token<TagTokenType> currentToken = null;
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
                        throw new ParseException(parseContext, "Invalid tag. Not expecting " + currentToken.getMatch());
                    }
                }
                case 10 -> {
                    if (currentToken.getValue() != null) {
                        throw new ParseException(parseContext, "Invalid tag. Expecting tag content but found " + currentToken.getMatch());
                    }
                    state = 11;
                }
                case 11 -> {
                    if (currentToken.getValue() != TagTokenType.Close) {
                        throw new ParseException(parseContext, "Invalid tag. Expecting close token.");
                    }
                    String tagText = previousToken.getMatch().trim();
                    Tag tag = parseTag(parseContext, tagText);
                    containers.peek().addChild(tag);
                    if (tag.isContainer()) {
                        containers.push(tag);
                    }
                    state = 0;
                }
                case 15 -> {
                    if (currentToken.getValue() != null) {
                        throw new ParseException(parseContext, "Invalid tag. Expecting end of tag but found " + currentToken.getMatch());
                    }
                    state = 16;
                }
                case 16 -> {
                    if (currentToken.getValue() != TagTokenType.EndClose) {
                        throw new ParseException(parseContext, "Invalid tag. Expecting end close token.");
                    }
                    String tagText = previousToken.getMatch().trim();
                    Tag container = containers.pop();
                    if (!tagText.equals(container.name())) {
                        throw new ParseException(parseContext, "Invalid tag end. Expecting end of tag " + container.name() + " but found " + tagText);
                    }
                    if (containers.isEmpty()) {
                        throw new ParseException(parseContext, "Unexpected end tag.");
                    }
                    state = 0;
                }
                case 20 -> {
                    //TODO remove while we are already iterating
                    //TODO what happens if end of line does not exists?
                    while (iterator.hasNext()) {
                        if (iterator.next().getValue() == TagTokenType.EndOfLine) {
                            parseContext.increaseLine();
                            break;
                        }
                    }
                    state = 0;
                }
                case 30 -> {
                    if (previousToken == null || previousToken.getValue() == null || previousToken.getValue() == TagTokenType.EndOfLine) {
                        Text text = new Text(parseContext.getPath(), parseContext.getLine(), currentToken.getMatch());
                        containers.peek().addChild(text);
                    }
                    parseContext.increaseLine();
                    state = 0;
                }
                case 40 -> {
                    Text text = new Text(parseContext.getPath(), parseContext.getLine(), currentToken.getMatch());
                    containers.peek().addChild(text);
                    state = 0;
                }
            }
        }
        if (state != 0) {
            throw new ParseException(parseContext, "Unexpected end of document. State: " + state + ".");
        }

        return new Document(containers.poll());
    }

    Tag parseTag(ParseContext parseContext, String tagText) {
        int index = tagText.indexOf(' ');
        if (index < 0) {
            throw new ParseException(parseContext, "Unexpected tag " + tagText + ".");
        }
        String tagName = tagText.substring(0, index);
        String tagAttributesAsText = tagText.substring(index + 1);

        TagRenderer renderer = TagRenderers.get(tagName);
        if (renderer == null) {
            throw new ParseException(parseContext, "Unknown tag renderer " + tagName + ".");
        }
        Tag tag = new Tag(parseContext.getPath(), parseContext.getLine(), renderer);
        parseTagAttributes(parseContext, tag, tagAttributesAsText);
        tag.validate();
        return tag;
    }

    void parseTagAttributes(ParseContext parseContext, Tag tag, String tagAttributesAsText) {
        Iterator<Token<TagAttributeTokenType>> iterator = tagAttributeTokenizer.tokenize(tagAttributesAsText);
        String currentAttrName = null;
        String currentExpression = null;
        int state = 0; // 0: start  1: attribute name  2: equals  3: double quotes  4: expression  5: double quotes
        while (iterator.hasNext()) {
            Token<TagAttributeTokenType> token = iterator.next();
            switch (state) {
                case 0 -> {
                    if (token.getValue() == TagAttributeTokenType.WhiteSpace) {
                        continue;
                    }
                    if (token.getValue() != null || !isJavaIdentifier(token.getMatch())) {
                        throw new ParseException(parseContext, "Expecting attribute name but found " + token.getMatch() + ".");
                    }
                    currentAttrName = token.getMatch();
                    state = 1;
                }
                case 1 -> {
                    if (token.getValue() == TagAttributeTokenType.WhiteSpace) {
                        continue;
                    }
                    if (token.getValue() != TagAttributeTokenType.Equal) {
                        throw new ParseException(parseContext, "Expecting '=' but found " + token.getMatch() + ".");
                    }
                    state = 2;
                }
                case 2 -> {
                    if (token.getValue() == TagAttributeTokenType.WhiteSpace) {
                        continue;
                    }
                    if (token.getValue() != TagAttributeTokenType.DoubleQuotes) {
                        throw new ParseException(parseContext, "Expecting '\"' but found " + token.getMatch() + ".");
                    }
                    currentExpression = "";
                    state = 3;
                }
                case 3 -> {
                    if (token.getValue() == TagAttributeTokenType.DoubleQuotes) {
                        state = 5;
                    } else {
                        currentExpression += token.getMatch();
                    }
                }
            }
            if (state == 5) {
                Expression expression = parseExpression(parseContext, currentExpression);
                tag.addAttribute(currentAttrName, expression);
                currentAttrName = null;
                currentExpression = null;
                state = 0;
            }
        }
        if (state != 0) {
            throw new ParseException(parseContext, "Incomplete attribute.");
        }
    }

    Expression parseExpression(ParseContext parseContext, String expressionAsText) {
        try {
            return expressionParser.parseExpression(expressionAsText);
        } catch (org.springframework.expression.ParseException e) {
            throw new ParseException(parseContext, "Unable to parse expression" + expressionAsText + ". " + e.getMessage(), e);
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
}
