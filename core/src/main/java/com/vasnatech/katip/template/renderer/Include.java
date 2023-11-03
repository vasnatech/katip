package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.nio.file.Path;

public class Include extends LeafRenderer {

    @Override
    public String name() {
        return "include";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "path");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression pathExpr = tag.attributes().get("path");
        String value = getValue(tag, renderContext, pathExpr);
        if (value != null) {
            Path path = Path.of(value);
            Document document = renderContext.project().documents().get(path);
            if (document != null) {
                renderChildren(document.root(), output, renderContext);
            }
        }
    }
}
