package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.Scope;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionExpression implements MemberExpression {

    final String name;
    final List<Expression> parameters;

    public FunctionExpression(String name) {
        this(name, new ArrayList<>(2));
    }

    public FunctionExpression(String name, List<Expression> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public void addParameter(Expression expression) {
        parameters.add(expression);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Object get(Object parent, RenderContext renderContext) {
        if (parent == null || parent instanceof Scope) {
            return renderContext.invokeFunction(
                    name,
                    parameters.stream().map(expression -> expression.get(renderContext)).toArray()
            );
        }
        return renderContext.invokeMethod(
                parent,
                name,
                parameters.stream().map(expression -> expression.get(renderContext)).toArray()
        );
    }

    @Override
    public void set(Object parent, Object value, RenderContext renderContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return parameters.stream().map(String::valueOf).collect(Collectors.joining(", ", name + "(", ")"));
    }
}
