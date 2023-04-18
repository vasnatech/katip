package com.vasnatech.katip.template.expression.function;

import java.util.HashMap;
import java.util.Map;

public final class PureFunctions {

    private PureFunctions() {
    }

    static final Map<String, PureFunction> FUNCTIONS = new HashMap<>();

    public static PureFunction get(String name) {
        return FUNCTIONS.get(name);
    }

    public static void register(PureFunction function) {
        FUNCTIONS.put(function.name(), function);
    }

    static {
        register(new Add());
        register(new Subtract());
        register(new Multiply());
        register(new Divide());
        register(new Mod());

        register(new And());
        register(new Or());
        register(new Xor());
        register(new Not());

        register(new Equals());
        register(new GreaterThan());
        register(new LessThan());
    }
}
