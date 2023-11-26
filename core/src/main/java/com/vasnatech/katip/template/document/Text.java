package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.RenderContext;
import com.vasnatech.katip.template.renderer.RenderException;

import java.io.IOException;
import java.nio.file.Path;

public class Text implements Part {


    final Path path;
    final int line;
    final String text;

    public Text(Path path, int line, String text) {
        this.path = path;
        this.line = line;
        this.text = text;
    }

    @Override
    public Path path() {
        return path;
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public void render(Output out, RenderContext renderContext) throws RenderException {
        try {
            out.append(text);
        } catch (IOException e) {
            throw new RenderException(path, line, "Unable to write to file " + out.path() + ".", e);
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
