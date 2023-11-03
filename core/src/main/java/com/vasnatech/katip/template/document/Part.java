package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.RenderContext;
import com.vasnatech.katip.template.renderer.RenderException;

public interface Part {

    void render(Output out, RenderContext renderContext) throws RenderException;
}
