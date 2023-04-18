package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class If extends ContainerRenderer {

    @Override
    public String name() {
        return "if";
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
        Expression condition = tag.attributes().get("condition");
        Object value = condition.get(renderContext);

        if ( (value != null) && (!(value instanceof Boolean) || (Boolean)value) ) {
            renderChildren(tag, appendable, renderContext.createSubContext());
        }
    }
}
