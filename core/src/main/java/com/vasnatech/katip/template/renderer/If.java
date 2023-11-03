package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

public class If extends ContainerRenderer {

    @Override
    public String name() {
        return "if";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "condition");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression condition = tag.attributes().get("condition");
        Object value = getValue(tag, renderContext, condition);

        if ( (value != null) && (!(value instanceof Boolean) || (Boolean)value) ) {
            renderChildren(tag, output, renderContext.createSubContext());
        }
    }
}
