package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.renderer.RenderContext;

public class ChainedExpression implements ChainableExpression {

    final Expression parentExpression;
    final ChainableExpression childExpression;

    public ChainedExpression(Expression parentExpression, ChainableExpression childExpression) {
        this.parentExpression = parentExpression;
        this.childExpression = childExpression;
    }

    @Override
    public Object get(Object parent, RenderContext renderContext) {
        Object newParent = parentExpression == null ? parent : parentExpression.get(parent, renderContext);
        return childExpression.get(newParent, renderContext);
    }

    @Override
    public void set(Object parent, Object value, RenderContext renderContext) {
        Object newParent = parentExpression == null ? parent : parentExpression.get(parent, renderContext);
        childExpression.set(newParent, value, renderContext);
    }

    @Override
    public String toString() {
        return (parentExpression == null ? "" : parentExpression + ".") + childExpression;
    }
}
