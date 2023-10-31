package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.expression.DefaultEvaluationContext;
import com.vasnatech.commons.type.Scope;
import com.vasnatech.katip.template.Project;

import java.util.Map;

public class DefaultRenderContext extends DefaultEvaluationContext implements RenderContext {

    Project project;

    public DefaultRenderContext(Project project, Map<String, Object> variables) {
        this(project, new Scope(variables));
    }

    DefaultRenderContext(Project project, Scope scope) {
        super(scope);
        this.project = project;
    }

    @Override
    public Project project() {
        return project;
    }

    public RenderContext createSubContext() {
        return new DefaultRenderContext(project, new Scope(scope()));
    }
}
