package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.renderer.RenderContext;

public class ConstantExpression implements Expression {

    final Object value;

    public ConstantExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object get(Object parent, RenderContext renderContext) {
        return value;
    }

    @Override
    public void set(Object parent, Object value, RenderContext renderContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
