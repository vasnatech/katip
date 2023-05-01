package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.text.token.Token;
import com.vasnatech.commons.text.token.Tokenizer;
import com.vasnatech.katip.template.document.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

public class DefaultParserTest {

    @Test
    void parse() throws IOException {
        DefaultParser parser = new DefaultParser();
        Document document = parser.parse(Resources.asString(DefaultParserTest.class, "../mysql-template"));
        System.out.println(document);
    }



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
