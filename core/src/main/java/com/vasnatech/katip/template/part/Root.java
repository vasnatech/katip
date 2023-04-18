package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class Root extends ContainerRenderer {

    @Override
    public String name() {
        return "root";
    }

    @Override
    public int minNumberOfParameters() {
        return 0;
    }

    @Override
    public int maxNumberOfParameters() {
        return 0;
    }

    @Override
    public void render(Tag tag, Appendable appendable, RenderContext renderContext) throws IOException {
        renderChildren(tag, appendable, renderContext);
    }
}
