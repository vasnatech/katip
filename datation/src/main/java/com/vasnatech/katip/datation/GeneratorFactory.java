package com.vasnatech.katip.datation;

public abstract class GeneratorFactory {

    static GeneratorFactory instance;

    public static GeneratorFactory instance() {
        return instance;
    }

    public static GeneratorFactory instance(GeneratorFactory instance) {
        GeneratorFactory.instance = instance;
        return instance;
    }

    public abstract Generator create();
}
