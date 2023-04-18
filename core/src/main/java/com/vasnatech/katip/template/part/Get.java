package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class Get extends LeafRenderer {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public int minNumberOfParameters() {
        return 1;
    }

    @Override
    public int maxNumberOfParameters() {
        return 1;
    }

    @Override
    public void render(Tag tag, Appendable appendable, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        Object value = keyExpr.get(renderContext);
        if (value != null) {
            appendable.append(value.toString());
        }
    }
}
