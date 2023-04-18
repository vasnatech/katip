package com.vasnatech.katip.template.expression.function;

public interface BinaryBooleanFunction extends BooleanFunction {

    Boolean invokeBoolean(Boolean left, Boolean right);

    @Override
    default Object invoke(Object[] params) {
        return invokeBoolean((Boolean) params[0], (Boolean) params[1]);
    }

    @Override
    default int minNumberOfParameters() {
        return 2;
    }

    @Override
    default int maxNumberOfParameters() {
        return 2;
    }
}
