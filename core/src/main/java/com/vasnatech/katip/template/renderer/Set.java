package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;

public class Set extends LeafRenderer {

    @Override
    public String name() {
        return "set";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExist(tag, "key", "value");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        Expression valueExpr = tag.attributes().get("value");
        Object value = valueExpr.get(renderContext);
        keyExpr.set(value, renderContext);
    }
}
