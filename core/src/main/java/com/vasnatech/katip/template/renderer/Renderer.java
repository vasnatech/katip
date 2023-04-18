package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public interface Renderer {

    default void render(Document document, OutputStream out, Map<String, Object> parameters) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            render(document, writer, parameters);
        }
    }

    default void render(Document document, Writer out, Map<String, Object> parameters) throws IOException {
        render(document, (Appendable)out, parameters);
    }

    void render(Document document, Appendable out, Map<String, Object> parameters) throws IOException;
}
