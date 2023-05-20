package com.vasnatech.katip.template.renderer;

import java.util.Map;

public abstract class DocumentRendererFactory {

    static DocumentRendererFactory INSTANCE;

    public static DocumentRendererFactory instance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultDocumentRendererFactory();
        }
        return INSTANCE;
    }

    public static void instance(DocumentRendererFactory factory) {
        DocumentRendererFactory.INSTANCE = factory;
    }

    public DocumentRenderer create() {
        return create(Map.of());
    }

    public abstract DocumentRenderer create(Map<String, ?> config);
}
