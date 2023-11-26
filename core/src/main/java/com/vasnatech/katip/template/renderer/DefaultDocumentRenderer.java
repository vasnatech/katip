package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.Output;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public record DefaultDocumentRenderer(Map<String, ?> config) implements DocumentRenderer {

    @Override
    public void render(Project project, Document document, Output out, Map<String, Object> parameters) throws IOException {
        RenderContext renderContext = new DefaultRenderContext(
                project,
                parameters,
                Optional.of("debugEnabled").map(config::get).map(Boolean.class::cast).orElse(Boolean.FALSE)
        );

        document.root().render(out, renderContext);
    }
}
