package com.vasnatech.katip.template.renderer;

import java.util.Map;

public class DefaultRendererFactory extends RendererFactory {

    @Override
    public Renderer create(Map<String, ?> config) {
        return new DefaultRenderer();
    }
}
