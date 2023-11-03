package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.io.IOException;

public class Get extends LeafRenderer {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression keyExpr = tag.attributes().get("key");
        Object value = getValue(tag, renderContext, keyExpr);
        if (value != null) {
            if (value instanceof Tag blockTag) {
                RenderContext subRendererContext = renderContext.createSubContext();
                renderChildren(blockTag, output, subRendererContext);
            } else {
                try {
                    output.append(value.toString());
                } catch (IOException e) {
                    throw new RenderException(tag.path(), tag.line(), "Unable to write to file " + output.path() + ".", e);
                }
            }
        }
    }
}
