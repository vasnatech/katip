package com.vasnatech.katip.template.renderer;

import com.vasnatech.commons.collection.Iterators;
import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.document.Tag;
import org.springframework.expression.Expression;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class Foreach extends ContainerRenderer {

    @Override
    public String name() {
        return "foreach";
    }

    @Override
    public void validate(Tag tag) throws RenderException {
        validateAllAttributesExist(tag, "items");
        validateOneAttributeExists(tag, "iterator", "item");
    }

    @Override
    public void render(Tag tag, Output output, RenderContext renderContext) throws RenderException {
        Expression iteratorExpr = tag.attributes().get("iterator");
        Expression itemExpr = tag.attributes().get("item");
        Expression itemsExpr = tag.attributes().get("items");
        Object items = getValue(tag, renderContext, itemsExpr);
        Iterator<?> iterator;
        if (items == null) {
            iterator = Collections.emptyIterator();
        } else if (items instanceof Iterable<?> iterable) {
            iterator = iterable.iterator();
        } else if (items instanceof Map<?, ?> map) {
            iterator = map.entrySet().iterator();
        } else if (items instanceof Iterator<?> ite) {
            iterator = ite;
        } else if (items.getClass().isArray()) {
            iterator = Iterators.from((Object[]) items);
        } else if (items instanceof Stream<?> stream) {
            iterator = stream.iterator();
        } else {
            iterator = Collections.emptyIterator();
        }

        int index = 0;
        while (iterator.hasNext()) {
            RenderContext subRendererContext = renderContext.createSubContext();
            Object item = iterator.next();
            if (itemExpr != null) {
                setValue(tag, renderContext, itemExpr, item);
            }
            if (iteratorExpr != null) {
                EnrichedItem enrichedItem = new EnrichedItem(index, index == 0, !iterator.hasNext(), item);
                setValue(tag, renderContext, iteratorExpr, enrichedItem);
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
