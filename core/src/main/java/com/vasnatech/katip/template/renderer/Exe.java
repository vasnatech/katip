package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.Map;

public class Exe extends LeafRenderer {

    @Override
    public String name() {
        return "exe";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "expression");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression expression = tag.attributes().get("expression");
        getValue(tag, renderContext, expression);
        debug(tag, renderContext, Map.of("expression", expression.getExpressionString()));
    }
}
