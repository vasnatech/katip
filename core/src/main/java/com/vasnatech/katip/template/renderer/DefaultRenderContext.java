package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.type.Scope;
import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.renderer.function.Functions;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.Optional;

public class DefaultRenderContext implements RenderContext {

    Project project;
    Scope scope;
    StandardEvaluationContext evaluationContext;
    boolean debugEnabled;

    public DefaultRenderContext(Project project, Map<String, Object> variables, boolean debugEnabled) {
        this(project, new Scope(variables), debugEnabled);
    }

    DefaultRenderContext(Project project, Scope scope, boolean debugEnabled) {
        this.project = project;
        this.scope = scope;
        this.debugEnabled = debugEnabled;
        this.evaluationContext = new StandardEvaluationContext(scope);
        this.evaluationContext.addPropertyAccessor(new ScopeAccessor());
        this.evaluationContext.addPropertyAccessor(new MapAccessor());
        Functions.methods().forEach((name, method) -> evaluationContext.registerFunction(name, method));
    }

    @Override
    public Project project() {
        return project;
    }

    @Override
    public Scope scope() {
        return scope;
    }

    @Override
    public EvaluationContext evaluationContext() {
        return evaluationContext;
    }

    public RenderContext createSubContext() {
        return new DefaultRenderContext(project, new Scope(scope()), debugEnabled);
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
