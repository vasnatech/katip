package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;

public class Block extends ContainerRenderer {

    @Override
    public String name() {
        return "block";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExists(tag, "key");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression keyExpr = tag.attributes().get("key");
        keyExpr.set(tag, renderContext);
    }
}
