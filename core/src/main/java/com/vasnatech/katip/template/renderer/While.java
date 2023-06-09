package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.expression.Expression;

import java.io.IOException;

public class While extends ContainerRenderer {

    @Override
    public String name() {
        return "while";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExist(tag, "condition");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression condition = tag.attributes().get("condition");
        Object value = condition.get(renderContext);

        while ( (value != null) && (!(value instanceof Boolean) || (Boolean)value) ) {
            renderChildren(tag, output, renderContext.createSubContext());
            value = condition.get(renderContext);
        }
    }

}
