package com.vasnatech.katip.template.parser;

import java.util.Map;

public abstract class ParserFactory {

    static ParserFactory INSTANCE;

    public static ParserFactory instance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultParserFactory();
        }
        return INSTANCE;
    }

    public static void instance(ParserFactory factory) {
        ParserFactory.INSTANCE = factory;
    }

    public abstract Parser create(Map<String, ?> config);
}
