package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.Map;

public class While extends ContainerRenderer {

    @Override
    public String name() {
        return "while";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "condition");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression condition = tag.attributes().get("condition");
        Object value = getValue(tag, renderContext, condition);
        while ( (value != null) && (!(value instanceof Boolean) || (Boolean)value) ) {
            debug(tag, renderContext, Map.of("condition", condition.getExpressionString()));
            renderChildren(tag, output, renderContext.createSubContext());
            value = getValue(tag, renderContext, condition);
        }
    }

}
