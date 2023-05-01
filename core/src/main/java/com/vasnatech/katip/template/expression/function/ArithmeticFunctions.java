package com.vasnatech.katip.template.expression.function;

import java.util.function.BinaryOperator;

public interface ArithmeticFunctions {

    ArithmeticFunction ADD = new ArithmeticFunction("add", Long::sum, Double::sum);
    ArithmeticFunction SUBTRACT = new ArithmeticFunction("subtract", (a, b) -> a - b, (a, b) -> a - b);
    ArithmeticFunction MULTIPLY = new ArithmeticFunction("multiply", (a, b) -> a * b, (a, b) -> a * b);
    ArithmeticFunction DIVIDE = new ArithmeticFunction("divide", (a, b) -> a / b, (a, b) -> a / b);
    ArithmeticFunction MOD = new ArithmeticFunction("mod", (a, b) -> a % b, (a, b) -> a % b);

    class ArithmeticFunction extends AbstractPureFunction {
        BinaryOperator<Long> longOperator;
        BinaryOperator<Double> doubleOperator;

        ArithmeticFunction(String name, BinaryOperator<Long> longOperator, BinaryOperator<Double> doubleOperator) {
            super(name, 2, 2);
            this.longOperator = longOperator;
            this.doubleOperator = doubleOperator;
        }
        @Override
        public Object invoke(Object[] params) {
            if (params[0] instanceof Double
                    || params[0] instanceof Float
                    || params[1] instanceof Double
                    || params[1] instanceof Float) {
                return doubleOperator.apply(((Number)params[0]).doubleValue(), ((Number)params[1]).doubleValue());
            }
            return longOperator.apply(((Number)params[0]).longValue(), ((Number)params[1]).longValue());
        }
        @Override
        public Class<?> returnType() {
            return Number.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return Number.class;
        }
    }
}
