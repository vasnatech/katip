package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class Text implements Part {

    final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(Output out, RenderContext renderContext) throws IOException {
        out.append(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
