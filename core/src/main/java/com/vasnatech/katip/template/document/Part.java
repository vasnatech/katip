package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.RenderContext;
import com.vasnatech.katip.template.renderer.RenderException;

import java.nio.file.Path;

public interface Part {

    Path path();

    int line();

    void render(Output out, RenderContext renderContext) throws RenderException;
}
