package com.vasnatech.katip.template.expression.function;

public interface PureFunction {

    String name();

    Object invoke(Object[] params);

    Class<?> returnType();

    int minNumberOfParameters();

    int maxNumberOfParameters();

    Class<?> parameterType(int index);

    default boolean validateParameters(Object[] parameters) {
        if (parameters == null
                || parameters.length < minNumberOfParameters()
                || parameters.length > maxNumberOfParameters()
        ) {
            return false;
        }
        for (int index = 0; index < parameters.length; ++index) {
            if (!parameterType(index).isInstance(parameters[index])) {
                return false;
            }
        }
        return true;
    }
}
