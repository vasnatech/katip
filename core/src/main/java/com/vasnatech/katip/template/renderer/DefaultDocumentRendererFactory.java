package com.vasnatech.katip.template.renderer;

import java.util.Map;

public class DefaultDocumentRendererFactory extends DocumentRendererFactory {

    @Override
    public DocumentRenderer create(Map<String, ?> config) {
        return new DefaultDocumentRenderer(config);
    }
}
