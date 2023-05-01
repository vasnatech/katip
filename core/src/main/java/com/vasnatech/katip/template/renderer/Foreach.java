package com.vasnatech.katip.template.renderer;

import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.expression.Expression;
import com.vasnatech.katip.template.document.Tag;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Foreach extends ContainerRenderer {

    @Override
    public String name() {
        return "foreach";
    }

    @Override
    public void validate(Tag tag) throws IOException {
        validateAllAttributesExists(tag, "items");
        validateOneAttributeExists(tag, "iterator", "item");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws IOException {
        Expression iteratorExpr = tag.attributes().get("iterator");
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

        int index = 0;
        AtomicInteger length = new AtomicInteger();
        while (iterator.hasNext()) {
            RenderContext subRendererContext = renderContext.createSubContext();
            Object item = iterator.next();
            if (itemExpr != null) {
                itemExpr.set(item, subRendererContext);
            }
            if (iteratorExpr != null) {
                EnrichedItem enrichedItem = new EnrichedItem(index, index == 0, !iterator.hasNext(), item);
                iteratorExpr.set(enrichedItem, subRendererContext);
            }
            renderChildren(tag, output, subRendererContext);
            ++index;
        }
    }

    public static class EnrichedItem {
        final int index;
        final boolean first;
        final boolean last;
        final Object item;

        EnrichedItem(int index, boolean first, boolean last, Object item) {
            this.index = index;
            this.first = first;
            this.last = last;
            this.item = item;
        }

        public int getIndex() {
            return index;
        }

        public Object getItem() {
            return item;
        }

        public boolean isFirst() {
            return first;
        }

        public boolean isMiddle() {
            return !first && !last;
        }

        public boolean isLast() {
            return last;
        }

        @Override
        public String toString() {
            return String.valueOf(item);
        }
    }
}
