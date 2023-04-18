package com.vasnatech.katip.template.expression.function;

public abstract class ArithmeticFunction implements PureFunction {

    @Override
    public Class<?> returnType() {
        return Number.class;
    }

    @Override
    public int minNumberOfParameters() {
        return 2;
    }

    @Override
    public int maxNumberOfParameters() {
        return 2;
    }

    @Override
    public Class<?> parameterType(int index) {
        return Number.class;
    }

    @Override
    public Number invoke(Object[] params) {
        if (params[0] instanceof Double
                || params[0] instanceof Float
                || params[1] instanceof Double
                || params[1] instanceof Float) {
            return invokeDouble(((Number)params[0]).doubleValue(), ((Number)params[1]).doubleValue());
        }
        return invokeLong(((Number)params[0]).longValue(), ((Number)params[1]).longValue());
    }

    protected abstract Long invokeLong(Long left, Long right);

    protected abstract Double invokeDouble(Double left, Double right);
}
