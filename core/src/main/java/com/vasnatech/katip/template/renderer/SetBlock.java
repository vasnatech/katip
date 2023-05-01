package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.expression.Expression;

import java.io.IOException;
import java.io.StringWriter;

public class SetBlock extends ContainerRenderer {

    @Override
    public String name() {
        return "set-block";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExists(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        StringWriter writer = new StringWriter();
        renderChildren(tag, output.create(writer), renderContext);
        Object value = writer.toString();
        keyExpr.set(value, renderContext);
    }
}
