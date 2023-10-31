package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.expression.EvaluationContext;
import com.vasnatech.katip.template.Project;

public interface RenderContext extends EvaluationContext {

    Project project();

    RenderContext createSubContext();
}
