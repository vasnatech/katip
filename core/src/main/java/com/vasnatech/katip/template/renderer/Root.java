package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;

import java.util.Map;

public final class Root extends ContainerRenderer {

    public static final Root INSTANCE = new Root();

    public static Root instance() {
        return INSTANCE;
    }

    private Root() {
    }

    @Override
    public String name() {
        return "root";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        debug(tag, renderContext, Map.of());
        renderChildren(tag, output, renderContext);
    }
}
