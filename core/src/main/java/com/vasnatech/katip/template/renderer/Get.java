package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.expression.Expression;
import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;

public class Get extends LeafRenderer {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExist(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        Object value = keyExpr.get(renderContext);
        if (value != null) {
            if (value instanceof Tag blockTag) {
                RenderContext subRendererContext = renderContext.createSubContext();
                renderChildren(blockTag, output, subRendererContext);
            } else {
                output.append(value.toString());
            }
        }
    }
}
