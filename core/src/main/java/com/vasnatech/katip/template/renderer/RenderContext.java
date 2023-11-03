package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.type.Scope;
import com.vasnatech.katip.template.Project;
import org.springframework.expression.EvaluationContext;

public interface RenderContext {

    Project project();

    Scope scope();

    EvaluationContext evaluationContext();

    RenderContext createSubContext();
}
