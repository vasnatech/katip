package com.vasnatech.katip.template.parser;

import java.util.Map;

public class DefaultParserFactory extends ParserFactory {

    @Override
    public Parser create(Map<String, ?> config) {
        return new DefaultParser();
    }
}
