package com.vasnatech.katip.template.expression.function;

public class Add extends ArithmeticFunction {

    @Override
    public String name() {
        return "add";
    }

    @Override
    protected Long invokeLong(Long left, Long right) {
        return left + right;
    }

    @Override
    protected Double invokeDouble(Double left, Double right) {
        return left + right;
    }
}
