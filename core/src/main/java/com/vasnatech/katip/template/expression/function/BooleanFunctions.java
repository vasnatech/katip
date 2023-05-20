package com.vasnatech.katip.template.expression.function;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public interface BooleanFunctions {

    BinaryBooleanFunction AND = new BinaryBooleanFunction("and", (a, b) -> a && b);
    BinaryBooleanFunction OR = new BinaryBooleanFunction("or", (a, b) -> a || b);
    BinaryBooleanFunction XOR = new BinaryBooleanFunction("xor", (a, b) -> a ^ b);
    UnaryBooleanFunction NOT = new UnaryBooleanFunction("not", a -> !a);


    abstract class BooleanFunction extends AbstractPureFunction {
        BooleanFunction(String name, int minNumberOfParameters, int maxNumberOfParameters) {
            super(name, minNumberOfParameters, maxNumberOfParameters);
        }
        @Override
        public Class<?> returnType() {
            return Boolean.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return Boolean.class;
        }
    }


    class UnaryBooleanFunction extends BooleanFunction {
        Function<Boolean, Boolean> function;

        UnaryBooleanFunction(String name, Function<Boolean, Boolean> function) {
            super(name, 1, 1);
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            return function.apply((Boolean) params[0]);
        }
    }

    class BinaryBooleanFunction extends BooleanFunction {
        BinaryOperator<Boolean> function;

        BinaryBooleanFunction(String name, BinaryOperator<Boolean> function) {
            super(name, 2, Integer.MAX_VALUE);
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            return Stream.of(params)
                    .map(Boolean.class::cast)
                    .reduce(function)
                    .orElse(Boolean.FALSE);
        }
    }
}
