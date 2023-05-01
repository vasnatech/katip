package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;
import java.nio.file.Path;

public class Include extends LeafRenderer {

    @Override
    public String name() {
        return "include";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExists(tag, "path");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression pathExpr = tag.attributes().get("path");
        Object value = pathExpr.get(renderContext);
        if (value != null) {
            Path path = Path.of(String.valueOf(value));
            Document document = renderContext.project().documents().get(path);
            if (document != null) {
                renderChildren(document.root(), output, renderContext);
            }
        }
    }
}
