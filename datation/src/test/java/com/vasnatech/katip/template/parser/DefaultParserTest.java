package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.katip.template.document.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DefaultParserTest {

    @Test
    void parse() throws IOException {
        DefaultParser parser = new DefaultParser();
        Document document = parser.parse(Resources.asString("sql/mysql/mysql.katip"));
        System.out.println(document);
    }
}
