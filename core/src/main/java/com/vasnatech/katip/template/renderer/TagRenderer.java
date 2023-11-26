package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Part;
import com.vasnatech.katip.template.document.Tag;
import com.vasnatech.katip.template.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
            return null;
            //throw new RenderException(tag.path(), tag.line(), "Unable to get value of expression " + expression.getExpressionString() + ".", e);
        }
    }

    default <T> T getValue(Tag tag, RenderContext renderContext, Expression expression, Class<T> type) throws RenderException {
        try {
            return expression.getValue(renderContext.evaluationContext(), type);
        } catch (ExpressionException e) {
            return null;
            //throw new RenderException(tag.path(), tag.line(), "Unable to get value of expression " + expression.getExpressionString() + ".", e);
        }
    }

    default <T> void setValue(Tag tag, RenderContext renderContext, Expression expression, T value) throws RenderException {
        try {
            expression.setValue(renderContext.evaluationContext(), value);
        } catch (ExpressionException e) {
            //throw new RenderException(tag.path(), tag.line(), "Unable to set value of expression " + expression.getExpressionString() + ".", e);
        }
    }

    default void debug(Tag tag, RenderContext renderContext, Map<String, ?> attributeValues) {
        if (renderContext.isDebugEnabled()) {
            String message = attributeValues.entrySet().stream()
                    .map(e -> e.getKey() + "=\"" + e.getValue()  + "\"")
                    .collect(Collectors.joining(
                            " ",
                            StringUtils.rightPad(StringUtils.abbreviate(tag.path().toString(), 50), 50) + ":" + StringUtils.leftPad(String.valueOf(tag.line()), 3) + " <" + name() + " ",
                            ">")
                    );
            Log.debug(message);
        }
    }

//    default void debug(Tag tag, RenderContext renderContext, Map<String, ?> attributeValues) {
//        if (renderContext.isDebugEnabled()) {
//            String message = tag.attributes().entrySet().stream()
//                    .map(e -> e.getKey() + "=\"" + Optional.of(e.getKey()).map(attributeValues::get).map(Objects::toString).orElse(e.getValue().getExpressionString())  + "\"")
//                    .collect(Collectors.joining(
//                            " ",
//                            StringUtils.rightPad(StringUtils.abbreviate(tag.path().toString(), 50), 50) + ":" + StringUtils.leftPad(String.valueOf(tag.line()), 3) + " <" + name() + " ",
//                            ">")
//                    );
//            Log.debug(message);
//        }
//    }
}
