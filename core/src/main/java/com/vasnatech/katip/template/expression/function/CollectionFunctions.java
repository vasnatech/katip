package com.vasnatech.katip.template.expression.function;

import com.vasnatech.commons.collection.Iterables;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

public interface CollectionFunctions {

    CollectionFunction FIRST = new CollectionFunction(
            "first",
            it -> {
                if (it instanceof LinkedList<?> list)
                    return list.isEmpty() ? null : list.getFirst();
                if (it instanceof List<?> list)
                    return list.isEmpty() ? null : list.get(0);
                if (it instanceof SortedSet<?> set)
                    return set.isEmpty() ? null : set.first();
                if (it.getClass().isArray()) {
                    int length = Array.getLength(it);
                    return length == 0 ? null : Array.get(it, 0);
                }
                if (it instanceof Iterable<?> iterable)
                    return Iterables.first(iterable);
                return null;
            }
    );

    CollectionFunction LAST = new CollectionFunction(
            "last",
            it -> {
                if (it instanceof LinkedList<?> list)
                    return list.isEmpty() ? null : list.getLast();
                if (it instanceof List<?> list)
                    return list.isEmpty() ? null : list.get(list.size() - 1);
                if (it instanceof SortedSet<?> set)
                    return set.isEmpty() ? null : set.last();
                if (it.getClass().isArray()) {
                    int length = Array.getLength(it);
                    return length == 0 ? null : Array.get(it, length - 1);
                }
                if (it instanceof Iterable<?> iterable)
                    return Iterables.last(iterable);
                return null;
            }
    );


    class CollectionFunction extends AbstractPureFunction {

        Function<Object, Object> function;

        protected CollectionFunction(String name, Function<Object, Object> function) {
            super(name, 1, 1);
            this.function = function;
        }

        @Override
        public Object invoke(Object[] params) {
            return function.apply(params[0]);
        }

        @Override
        public Class<?> returnType() {
            return Object.class;
        }

        @Override
        public Class<?> parameterType(int index) {
            return Iterable.class;
        }
    }
}
