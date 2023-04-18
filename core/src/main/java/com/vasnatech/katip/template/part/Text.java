package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public class Text implements Part {

    final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(Appendable appendable, RenderContext renderContext) throws IOException {
        appendable.append(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
