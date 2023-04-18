package com.vasnatech.katip.template.expression.function;

public class LessThan implements ComparisonFunction {

    @Override
    public String name() {
        return "lt";
    }

    @Override
    public Boolean invoke(Object[] params) {
        if (params[0] instanceof Double
                || params[0] instanceof Float
                || params[1] instanceof Double
                || params[1] instanceof Float) {
            return invokeDouble(((Number)params[0]).doubleValue(), ((Number)params[1]).doubleValue()) < 0;
        }
        return invokeLong(((Number)params[0]).longValue(), ((Number)params[1]).longValue()) < 0;
    }
}
