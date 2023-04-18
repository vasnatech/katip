package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Document;

import java.io.IOException;
import java.util.Map;

public class DefaultRenderer implements Renderer {

    @Override
    public void render(Document document, Appendable out, Map<String, Object> parameters) throws IOException {
        RenderContext renderContext = new DefaultRenderContext(parameters);

        document.root().render(out, renderContext);
    }
}
