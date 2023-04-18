package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.renderer.RenderContext;

public interface MemberExpression extends ChainableExpression {

    String name();

    Object get(Object parent, RenderContext renderContext);
}
