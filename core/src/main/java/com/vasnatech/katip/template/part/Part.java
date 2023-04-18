package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.Node;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;

public interface Part extends Node {

    void render(Appendable appendable, RenderContext renderContext) throws IOException;
}
