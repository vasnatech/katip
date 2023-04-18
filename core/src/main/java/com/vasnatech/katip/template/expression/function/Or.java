package com.vasnatech.katip.template.expression.function;

public class Or implements BinaryBooleanFunction {

    @Override
    public String name() {
        return "or";
    }

    @Override
    public Boolean invokeBoolean(Boolean left, Boolean right) {
        return left || right;
    }
}
