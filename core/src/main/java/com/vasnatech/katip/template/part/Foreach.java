package com.vasnatech.katip.template.part;

import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.renderer.RenderContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Foreach extends ContainerRenderer {

    @Override
    public String name() {
        return "foreach";
    }

    @Override
    public int minNumberOfParameters() {
        return 2;
    }

    @Override
    public int maxNumberOfParameters() {
        return 2;
    }

    @Override
    public void render(Tag tag, Appendable appendable, RenderContext renderContext) throws IOException {
        Expression itemExpr = tag.attributes().get("item");
        Expression itemsExpr = tag.attributes().get("items");
        Object items = itemsExpr.get(renderContext);
        Iterator<?> iterator;
        if (items == null) {
            iterator = Collections.emptyIterator();
        } else if (items instanceof Iterator) {
            iterator = (Iterator<?>) items;
        } else if (items instanceof Iterable) {
            iterator = ((Iterable<?>) items).iterator();
        } else if (items.getClass().isArray()) {
            iterator = List.of((Object[]) items).iterator();
        } else if (items instanceof Stream) {
            iterator = ((Stream<?>) items).iterator();
        } else {
            iterator = Collections.emptyIterator();
        }

        while (iterator.hasNext()) {
            RenderContext subRendererContext = renderContext.createSubContext();
            itemExpr.set(iterator.next(), subRendererContext);
            renderChildren(tag, appendable, subRendererContext);
        }
    }
}
