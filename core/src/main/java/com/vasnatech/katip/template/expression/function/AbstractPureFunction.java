package com.vasnatech.katip.template.expression.function;

public abstract class AbstractPureFunction implements PureFunction{

    protected String name;
    protected int minNumberOfParameters;
    protected int maxNumberOfParameters;

    protected AbstractPureFunction(String name, int minNumberOfParameters, int maxNumberOfParameters) {
        this.name = name;
        this.minNumberOfParameters = minNumberOfParameters;
        this.maxNumberOfParameters = maxNumberOfParameters;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int minNumberOfParameters() {
        return minNumberOfParameters;
    }

    @Override
    public int maxNumberOfParameters() {
        return maxNumberOfParameters;
    }
}
