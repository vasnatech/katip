package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.expression.Expression;

import java.io.IOException;

public class Exe extends LeafRenderer {

    @Override
    public String name() {
        return "exe";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExist(tag, "expression");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression expression = tag.attributes().get("expression");
        expression.get(renderContext);
    }
}
