package com.vasnatech.katip.template.expression.function;

public class Not implements BooleanFunction {

    @Override
    public String name() {
        return "not";
    }

    @Override
    public Boolean invoke(Object[] params) {
        return !((Boolean)params[0]);
    }

    @Override
    public int minNumberOfParameters() {
        return 1;
    }

    @Override
    public int maxNumberOfParameters() {
        return 1;
    }
}
