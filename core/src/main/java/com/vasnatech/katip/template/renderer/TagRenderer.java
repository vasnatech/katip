package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.expression.EvaluationContext;
import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Part;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface TagRenderer {

    String name();

    boolean isContainer();

    default boolean isLeaf() {
        return !isContainer();
    }

    void validate(Tag tag) throws IOException;

    EvaluationContext s = null;

    default void validateAllAttributesExist(Tag tag, String... attributeNames) throws IOException {

        String message = Arrays.stream(attributeNames)
                .filter(Predicate.not(tag.attributes()::containsKey))
                .map(attributeName -> name() + " tag should have attribute " + attributeName + " on line " + tag.line() + ".")
                .collect(Collectors.joining(" "));
        if (!message.isEmpty())
            throw new IOException(message);
    }

    default void validateOneAttributeExists(Tag tag, String... attributeNames) throws IOException {
        Arrays.stream(attributeNames)
                .filter(tag.attributes()::containsKey)
                .findAny()
                .orElseThrow(() -> {
                    String message = name() + " tag should have at least one of the attributes " + String.join(",", attributeNames) + " on line " + tag.line() + ".";
                    return new IOException(message);
                });
    }

    void render(Tag tag, Output output, RenderContext renderContext) throws IOException;

    default void renderChildren(Tag tag, Output out, RenderContext renderContext) throws IOException {
        for (Part child : tag.children()) {
            child.render(out, renderContext);
        }
    }
}
