package com.vasnatech.katip.datation.sql.mysql;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.katip.template.Document;
import com.vasnatech.katip.template.parser.Parser;
import com.vasnatech.katip.template.parser.ParserFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class MySqlTemplateTest {
    @Test
    void parse() throws IOException {
        Parser parser = ParserFactory.instance().create(Map.of());
        Document document = parser.parse(Resources.asString("mysql-template"));
        System.out.println(document);
    }
}
