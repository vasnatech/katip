package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public abstract class ContainerRenderer implements TagRenderer {

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String toString() {
        return name();
    }

    protected void renderChildren(Tag tag, Appendable appendable, RenderContext renderContext) throws IOException {
        for (Part child : tag.children()) {
            child.render(appendable, renderContext);
        }
    }
}
