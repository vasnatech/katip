package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.renderer.RenderContext;

public class PropertyExpression implements MemberExpression {

    final String name;

    public PropertyExpression(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Object get(Object parent, RenderContext renderContext) {
        return renderContext.getProperty(parent, name);
    }

    @Override
    public void set(Object parent, Object value, RenderContext renderContext) {
        renderContext.setProperty(parent, name, value);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
