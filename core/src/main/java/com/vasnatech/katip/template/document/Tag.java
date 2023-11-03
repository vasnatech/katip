package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.RenderContext;
import com.vasnatech.katip.template.renderer.RenderException;
import com.vasnatech.katip.template.renderer.TagRenderer;
import org.springframework.expression.Expression;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tag implements Part {

    final Path path;
    final int line;
    final TagRenderer renderer;
    final Map<String, Expression> attributes;
    final List<Part> children;

    public Tag(Path path, int line, TagRenderer renderer) {
        this.path = path;
        this.line = line;
        this.renderer = renderer;
        this.attributes = new LinkedHashMap<>();
        this.children = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "<" + renderer
                + attributes.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue().getExpressionString())
                    .collect(Collectors.joining(", ", "(", ")"))
                + children.stream()
                    .map(Part::toString)
                    .collect(Collectors.joining(""))
                + renderer + ">";
    }

    public String name() {
        return renderer.name();
    }

    public Path path() {
        return path;
    }

    public int line() {
        return line;
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
    public void render(Output out, RenderContext renderContext) throws RenderException {
        renderer.render(this, out, renderContext);
    }

    public void validate() {
        renderer.validate(this);
    }
}
