package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.expression.*;
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

    enum ExpressionTokenType {WhiteSpace, OpenParenthesis, CloseParenthesis, Dot, Comma, Quote}

    final Tokenizer<TagTokenType> tagTokenizer;
    final Tokenizer<TagAttributeTokenType> tagAttributeTokenizer;
    final Tokenizer<ExpressionTokenType> expressionTokenizer;

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
        expressionTokenizer = new Tokenizer<>(
                new Token<>("(", ExpressionTokenType.OpenParenthesis),
                new Token<>(")", ExpressionTokenType.CloseParenthesis),
                new Token<>(".", ExpressionTokenType.Dot),
                new Token<>(",", ExpressionTokenType.Comma),
                new Token<>("'", ExpressionTokenType.Quote),
                new Token<>(" ", ExpressionTokenType.WhiteSpace, false),
                new Token<>("\t", ExpressionTokenType.WhiteSpace, false),
                new Token<>("\b", ExpressionTokenType.WhiteSpace, false)
        );
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
        ExpressionParser expressionParser = new ExpressionParser(expressionAsText, line);
        return expressionParser.parse();
    }

    class ExpressionParser {
        final String expressionAsText;
        final Iterator<Token<ExpressionTokenType>> iterator;
        final AtomicInteger line;

        int state = 0;
        boolean goNext = true;
        Token<ExpressionTokenType> previousToken = null;
        Token<ExpressionTokenType> currentToken = null;


        ExpressionParser(String expressionAsText, AtomicInteger line) {
            this.expressionAsText = expressionAsText;
            this.iterator = expressionTokenizer.tokenize(expressionAsText);
            this.line = line;
        }

        Expression parse() {
            Expression currentExpression = null;
            while (iterator.hasNext() || state == 9 || !goNext) {
                if (state != 9 && goNext) {
                    previousToken = currentToken;
                    currentToken = iterator.next();
                }
                goNext = true;
                switch (state) {
                    case 0 -> {
                        if (currentToken.getValue() == ExpressionTokenType.Quote) {
                            currentExpression = new ConstantExpression("");
                            state = 15;
                        } else {
                            if (currentToken.getValue() != null) {
                                throw new RuntimeException("Expecting identifier, number, boolean or string but found '" + currentToken.getMatch() + "' at line " + line + ".");
                            }
                            Long number = parseLong(currentToken.getMatch());
                            if (number != null) {
                                currentExpression = new ConstantExpression(number);
                                if (iterator.hasNext()) {
                                    state = 11;
                                } else {
                                    state = 9;
                                }
                            } else {
                                Boolean bool = parseBoolean(currentToken.getMatch());
                                if (bool != null) {
                                    currentExpression = new ConstantExpression(bool);
                                    state = 9;
                                } else {
                                    goNext = false;
                                    state = 20;
                                }
                            }
                        }
                    }
                    case 11 -> { //currentExpression is number
                        if (currentToken.getValue() == ExpressionTokenType.Dot) {
                            state = 12;
                        } else {
                            state = 9;
                        }
                    }
                    case 12 -> {//currentExpression is number & last token was DOT
                        if (currentToken.getValue() != null) {
                            throw new RuntimeException("Expecting number but found '" + currentToken.getMatch() + "' at line " + line + ".");
                        }
                        Long number = parseLong(currentToken.getMatch());
                        if (number == null || number < 0) {
                            throw new RuntimeException("Expecting number but found '" + currentToken.getMatch() + "' at line " + line + ".");
                        }
                        ConstantExpression constantExpression = (ConstantExpression) currentExpression;
                        Double decimal = Double.valueOf(constantExpression + "." + number);
                        currentExpression = new ConstantExpression(decimal);
                        state = 9;
                    }
                    case 15 -> {//current expression is a string
                        if (currentToken.getValue() == ExpressionTokenType.Quote) {
                            previousToken = currentToken;
                            if (iterator.hasNext())
                                currentToken = iterator.next();
                            state = 9;
                        } else {
                            ConstantExpression constantExpression = (ConstantExpression) currentExpression;
                            currentExpression = new ConstantExpression(constantExpression + currentToken.getMatch());
                            state = 15;
                        }
                    }
                    case 20 -> {//current token is start of a property or a function
                        if (!isJavaIdentifier(currentToken.getMatch())) {
                            throw new RuntimeException("Expecting identifier but found '" + currentToken.getMatch() + "' at line " + line + ".");
                        }
                        if (iterator.hasNext()) {
                            previousToken = currentToken;
                            currentToken = iterator.next();
                            if (currentToken.getValue() == ExpressionTokenType.OpenParenthesis) {
                                FunctionExpression functionExpression = new FunctionExpression(previousToken.getMatch());
                                currentExpression = new ChainedExpression(currentExpression, functionExpression);
                                state = 30;
                                parseFunctionParameters(functionExpression);
                                if (iterator.hasNext()) {
                                    state = 21;
                                } else {
                                    state = 9;
                                }
                            }
                            else if (currentToken.getValue() == ExpressionTokenType.Dot) {
                                currentExpression = new ChainedExpression(currentExpression, new PropertyExpression(previousToken.getMatch()));
                                state = 20;
                            }
                            else if (currentToken.getValue() == ExpressionTokenType.Comma || currentToken.getValue() == ExpressionTokenType.CloseParenthesis) {
                                currentExpression = new ChainedExpression(currentExpression, new PropertyExpression(previousToken.getMatch()));
                                state = 9;
                            }
                            else {
                                throw new RuntimeException("Expecting '.' or '(' but found '" + currentToken.getMatch() + "' at line " + line + ".");
                            }
                        } else {
                            currentExpression = new ChainedExpression(currentExpression, new PropertyExpression(currentToken.getMatch()));
                            state = 9;
                        }
                    }
                    case 21 -> {
                        if (currentToken.getValue() == ExpressionTokenType.Dot) {
                            state = 20;
                        }
                        else if (currentToken.getValue() == ExpressionTokenType.Comma || currentToken.getValue() == ExpressionTokenType.CloseParenthesis) {
                            state = 9;
                        } else {
                            state = 20;
                            goNext = false;
                        }
                    }
                    case 9 -> {
                        return currentExpression;
                    }
                }
            }
            throw new RuntimeException("Unable to parse '" + expressionAsText + "' at line " + line + ".");
        }

        void parseFunctionParameters(FunctionExpression functionExpression) {
            while (iterator.hasNext() || !goNext) {
                if (goNext) {
                    previousToken = currentToken;
                    currentToken = iterator.next();
                }
                goNext = true;
                switch (state) {
                    case 30 -> {
                        if (currentToken.getValue() == ExpressionTokenType.CloseParenthesis) {
                            return;
                        }
                        state = 32;
                        goNext = false;
                    }
                    case 31 -> {
                        if (currentToken.getValue() == ExpressionTokenType.CloseParenthesis) {
                            return;
                        }
                        else if (currentToken.getValue() == ExpressionTokenType.Comma) {
                            state = 32;
                        }
                        else {
                            throw new RuntimeException("Expecting ',' or ')' but found '" + currentToken.getMatch() + "' at line " + line + ".");
                       }
                    }
                    case 32 -> {
                        if (currentToken.getValue() == null || currentToken.getValue() == ExpressionTokenType.Quote) {
                            state = 0;
                            goNext = false;
                            Expression expression = parse();
                            functionExpression.addParameter(expression);
                            state = 31;
                            goNext = false;
                        }
                    }
                }
            }
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
