package com.vasnatech.katip.template.expression.function;

import java.util.function.Function;

public interface ComparisonFunctions {

    ComparisonFunction EQUALS = new ComparisonFunction("eq", it -> it != null && it == 0);
    ComparisonFunction GREATER_THAN = new ComparisonFunction("gt", it -> it != null && it > 0);
    ComparisonFunction LESS_THAN = new ComparisonFunction("lt", it -> it != null && it < 0);

    class ComparisonFunction extends AbstractPureFunction {
        Function<Integer, Boolean> function;

        ComparisonFunction(String name, Function<Integer, Boolean> function) {
            super(name, 2, 2);
            this.function = function;
        }
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Object invoke(Object[] params) {
            Comparable left = (Comparable<?>) params[0];
            Comparable right = (Comparable<?>) params[1];
            Integer compare;
            if (left.getClass().isAssignableFrom(right.getClass())) {
                compare = left.compareTo(right);
            } else if (right.getClass().isAssignableFrom(left.getClass())) {
                compare = -1 * right.compareTo(left);
            } else if (left instanceof Double
                || left instanceof Float
                || right instanceof Double
                || right instanceof Float
            ) {
                compare = invokeDouble(((Number)left).doubleValue(), ((Number)right).doubleValue());
            } else if (left instanceof Long
                    || left instanceof Integer
                    || right instanceof Long
                    || right instanceof Integer
            ) {
                compare = invokeLong(((Number)left).longValue(), ((Number)right).longValue());
            } else {
                compare = null;
            }
            return function.apply(compare);
        }
        @Override
        public Class<?> returnType() {
            return Boolean.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return Comparable.class;
        }
        int invokeLong(long left, long right) {
            return Long.compare(left, right);
        }
        int invokeDouble(double left, double right) {
            return Double.compare(left, right);
        }
    }
}
