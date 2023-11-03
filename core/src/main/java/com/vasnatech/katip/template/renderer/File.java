package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.io.IOException;

public class File extends ContainerRenderer {

    @Override
    public String name() {
        return "file";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "path");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression pathExpr = tag.attributes().get("path");
        String pathObj = getValue(tag, renderContext, pathExpr);
        if (pathObj == null) {
            return;
        }
        try (Output file = output.createFile(pathObj)) {
            RenderContext subRendererContext = renderContext.createSubContext();
            renderChildren(tag, file, subRendererContext);
        } catch (IOException e) {
            throw new RenderException(tag.path(), tag.line(), "Unable to open file " + pathObj + ".", e);
        }
    }
}
