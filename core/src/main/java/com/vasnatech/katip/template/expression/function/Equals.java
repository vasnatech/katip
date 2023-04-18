package com.vasnatech.katip.template.expression.function;

public class Equals implements ComparisonFunction {

    @Override
    public String name() {
        return "eq";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Boolean invoke(Object[] params) {
        return ((Comparable)params[0]).compareTo(params[1]) == 0;
    }
}
