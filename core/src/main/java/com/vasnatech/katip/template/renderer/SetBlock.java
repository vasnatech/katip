package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.io.StringWriter;

public class SetBlock extends ContainerRenderer {

    @Override
    public String name() {
        return "set-block";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression keyExpr = tag.attributes().get("key");
        StringWriter writer = new StringWriter();
        renderChildren(tag, output.create(writer), renderContext);
        Object value = writer.toString();
        setValue(tag, renderContext, keyExpr, value);
    }
}
