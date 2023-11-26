package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.Map;

public class Case extends ContainerRenderer {

    @Override
    public String name() {
        return "case";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "value");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression valueExpr = tag.attributes().get("value");
        Object value = getValue(tag, renderContext, valueExpr);
        debug(tag, renderContext, Map.of("value", value));
        renderChildren(tag, output, renderContext.createSubContext());
    }
}
