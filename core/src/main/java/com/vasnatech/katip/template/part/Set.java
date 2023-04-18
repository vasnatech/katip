package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class Set extends LeafRenderer {

    @Override
    public String name() {
        return "set";
    }

    @Override
    public int minNumberOfParameters() {
        return 2;
    }

    @Override
    public int maxNumberOfParameters() {
        return 2;
    }

    @Override
    public void render(Tag tag, Appendable appendable, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        Expression valueExpr = tag.attributes().get("value");
        Object value = valueExpr.get(renderContext);
        keyExpr.set(value, renderContext);
    }
}
