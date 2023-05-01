package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.Output;

import java.io.IOException;
import java.util.Map;

public interface DocumentRenderer {

    void render(Project project, Document document, Output out, Map<String, Object> parameters) throws IOException;
}
