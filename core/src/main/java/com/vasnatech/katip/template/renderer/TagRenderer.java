package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Part;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface TagRenderer {

    String name();

    boolean isContainer();

    default boolean isLeaf() {
        return !isContainer();
    }

    void validate(Tag tag) throws RenderException;

    default void validateAllAttributesExist(Tag tag, String... attributeNames) throws RenderException {
        String message = Arrays.stream(attributeNames)
                .filter(Predicate.not(tag.attributes()::containsKey))
                .map(attributeName -> name() + " tag should have attribute " + attributeName + " at line " + tag.line() + ".")
                .collect(Collectors.joining(" "));
        if (!message.isEmpty())
            throw new RenderException(tag.path(), tag.line(), message);
    }

    default void validateOneAttributeExists(Tag tag, String... attributeNames) throws RenderException {
        Arrays.stream(attributeNames)
                .filter(tag.attributes()::containsKey)
                .findAny()
                .orElseThrow(() -> {
                    String message = name() + " tag should have at least one of the attributes " + String.join(",", attributeNames) + " at line " + tag.line() + ".";
                    return new RenderException(tag.path(), tag.line(), message);
                });
    }

    void render(Tag tag, Output output, RenderContext renderContext) throws RenderException;

    default void renderChildren(Tag tag, Output out, RenderContext renderContext) throws RenderException {
        for (Part child : tag.children()) {
            child.render(out, renderContext);
        }
    }

    @SuppressWarnings("unchecked")
    default <T> T getValue(Tag tag, RenderContext renderContext, Expression expression) throws RenderException {
        try {
            return (T) expression.getValue(renderContext.evaluationContext());
        } catch (ExpressionException e) {
            throw new RenderException(tag.path(), tag.line(), "Unable to get value of expression " + expression.getExpressionString() + ".", e);
        }
    }

    default <T> T getValue(Tag tag, RenderContext renderContext, Expression expression, Class<T> type) throws RenderException {
        try {
            return expression.getValue(renderContext.evaluationContext(), type);
        } catch (ExpressionException e) {
            throw new RenderException(tag.path(), tag.line(), "Unable to get value of expression " + expression.getExpressionString() + ".", e);
        }
    }

    default <T> void setValue(Tag tag, RenderContext renderContext, Expression expression, T value) throws RenderException {
        try {
            expression.setValue(renderContext.evaluationContext(), value);
        } catch (ExpressionException e) {
            throw new RenderException(tag.path(), tag.line(), "Unable to set value of expression " + expression.getExpressionString() + ".", e);
        }
    }
}
