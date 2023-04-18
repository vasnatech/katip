package com.vasnatech.katip.template.expression.function;

public interface ComparisonFunction extends PureFunction {

    @Override
    default Class<?> returnType() {
        return Boolean.class;
    }

    @Override
    default int minNumberOfParameters() {
        return 2;
    }

    @Override
    default int maxNumberOfParameters() {
        return 2;
    }

    @Override
    default Class<?> parameterType(int index) {
        return Comparable.class;
    }


    default int invokeLong(Long left, Long right){
        return left.compareTo(right);
    }

    default int invokeDouble(Double left, Double right) {
        return left.compareTo(right);
    }
}
