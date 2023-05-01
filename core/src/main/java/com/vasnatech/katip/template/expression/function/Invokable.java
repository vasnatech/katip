package com.vasnatech.katip.template.expression.function;

@FunctionalInterface
public
interface Invokable {
    Object invoke(Object[] params);
}
