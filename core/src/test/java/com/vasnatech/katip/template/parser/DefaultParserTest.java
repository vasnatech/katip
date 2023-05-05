package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class DefaultParserTest {

    @Test
    void test() {
        Tokenizer<DefaultParser.TagAttributeTokenType> tagAttributeTokenizer = new Tokenizer<>(
                new Token<>("=", DefaultParser.TagAttributeTokenType.Equal),
                new Token<>("\"", DefaultParser.TagAttributeTokenType.DoubleQuotes),
                new Token<>(" ", DefaultParser.TagAttributeTokenType.WhiteSpace, false),
                new Token<>("\t", DefaultParser.TagAttributeTokenType.WhiteSpace, false),
                new Token<>("\b", DefaultParser.TagAttributeTokenType.WhiteSpace, false)
        );

        Iterator<Token<DefaultParser.TagAttributeTokenType>> iterator = tagAttributeTokenizer.tokenize(" item=\"table\" items=\"schema.getTables()\"");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
