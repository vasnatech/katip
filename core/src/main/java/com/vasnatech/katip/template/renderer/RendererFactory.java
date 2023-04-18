package com.vasnatech.katip.template.renderer;

import java.util.Map;

public abstract class RendererFactory {

    static RendererFactory INSTANCE;

    public static RendererFactory instance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultRendererFactory();
        }
        return INSTANCE;
    }

    public static void instance(RendererFactory factory) {
        RendererFactory.INSTANCE = factory;
    }

    public abstract Renderer create(Map<String, ?> config);
}
