package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;

public class File extends ContainerRenderer {

    @Override
    public String name() {
        return "file";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExists(tag, "path");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression pathExpr = tag.attributes().get("path");
        Object pathObj = pathExpr.get(renderContext);
        if (pathObj == null) {
            return;
        }
        try (Output file = output.createFile(pathObj.toString())) {
            RenderContext subRendererContext = renderContext.createSubContext();
            renderChildren(tag, file, subRendererContext);
        } catch (IOException e) {
            throw e;
        }
    }
}
