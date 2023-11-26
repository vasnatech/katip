package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.Map;

public class Block extends ContainerRenderer {

    @Override
    public String name() {
        return "block";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression keyExpr = tag.attributes().get("key");
        debug(tag, renderContext, Map.of("key", keyExpr.getExpressionString()));
        setValue(tag, renderContext, keyExpr, tag);
    }
}
