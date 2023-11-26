package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;

import java.util.Map;

public class Else extends ContainerRenderer {

    @Override
    public String name() {
        return "else";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        debug(tag, renderContext, Map.of());
        renderChildren(tag, output, renderContext.createSubContext());
    }
}
