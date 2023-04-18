package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tag implements Part {

    final TagRenderer renderer;
    final Map<String, Expression> attributes;
    final List<Part> children;

    public Tag(TagRenderer renderer) {
        this.renderer = renderer;
        this.attributes = new LinkedHashMap<>();
        this.children = new ArrayList<>();
    }

    @Override
    public String toString() {
        return renderer
                + attributes.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining(", ", "(", ")"))
                + children.stream()
                    .map(Part::toString)
                    //.map(s -> s.replaceAll("\r", "\\r").replaceAll("\n", "\\n").replaceAll("\t", "\\t"))
                    .collect(Collectors.joining(""));
    }

    public TagRenderer renderer() {
        return renderer;
    }

    public boolean isContainer() {
        return renderer.isContainer();
    }

    public Map<String, Expression> attributes() {
        return attributes;
    }

    public void addAttribute(String name, Expression expression) {
        attributes.put(name, expression);
    }

    public List<Part> children() {
        return children;
    }

    public void addChild(Part child) {
        children.add(child);
    }

    @Override
    public void render(Appendable appendable, RenderContext renderContext) throws IOException {
        renderer.render(this, appendable, renderContext);
    }
}
