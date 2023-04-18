package com.vasnatech.katip.template.expression.function;

public interface BooleanFunction extends PureFunction {

    @Override
    default Class<?> returnType() {
        return Boolean.class;
    }

    @Override
    default Class<?> parameterType(int index) {
        return Boolean.class;
    }
}
