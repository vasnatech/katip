package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

public class Set extends LeafRenderer {

    @Override
    public String name() {
        return "set";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "key", "value");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression keyExpr = tag.attributes().get("key");
        Expression valueExpr = tag.attributes().get("value");
        Object value = getValue(tag, renderContext, valueExpr);
        setValue(tag, renderContext, keyExpr, value);
    }
}
