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
        register(CreateFunctions.CREATE_SET);
        register(CreateFunctions.CREATE_LIST);
        register(CreateFunctions.CREATE_MAP);

        register(ArithmeticFunctions.ADD);
        register(ArithmeticFunctions.SUBTRACT);
        register(ArithmeticFunctions.MULTIPLY);
        register(ArithmeticFunctions.DIVIDE);
        register(ArithmeticFunctions.MOD);

        register(BooleanFunctions.AND);
        register(BooleanFunctions.OR);
        register(BooleanFunctions.XOR);
        register(BooleanFunctions.NOT);

        register(ComparisonFunctions.EQUALS);
        register(ComparisonFunctions.GREATER_THAN);
        register(ComparisonFunctions.LESS_THAN);

        register(StringFunctions.TO_UPPER_CASE);
        register(StringFunctions.TO_LOWER_CASE);
        register(StringFunctions.CONCAT);
        register(StringFunctions.REPLACE);

        register(NamingFunctions.TO_SNAKE_CASE);
        register(NamingFunctions.FROM_SNAKE_CASE);
        register(NamingFunctions.TO_KEBAB_CASE);
        register(NamingFunctions.FROM_KEBAB_CASE);
        register(NamingFunctions.TO_PASCAL_CASE);
        register(NamingFunctions.FROM_PASCAL_CASE);
        register(NamingFunctions.TO_CAMEL_CASE);
        register(NamingFunctions.FROM_CAMEL_CASE);

        register(Join.INSTANCE);

        register(DateTimeFunctions.NOW);
    }
}
