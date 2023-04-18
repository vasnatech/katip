package com.vasnatech.katip.template.expression.function;

public class Xor implements BinaryBooleanFunction {

    @Override
    public String name() {
        return "xor";
    }

    @Override
    public Boolean invokeBoolean(Boolean left, Boolean right) {
        return left ^ right;
    }
}
