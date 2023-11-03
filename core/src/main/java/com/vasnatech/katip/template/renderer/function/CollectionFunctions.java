package com.vasnatech.katip.template.renderer.function;

import com.vasnatech.commons.collection.Iterables;
import com.vasnatech.commons.collection.Iterators;
import com.vasnatech.commons.type.Pair;
import com.vasnatech.commons.type.Triple;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectionFunctions {

    static Set<?> setOf(Object... objects) {
        return new LinkedHashSet<>(Set.of(objects));
    }

    static List<?> listOf(Object... objects) {
        return Arrays.asList(objects);
    }

    static Map<?, ?> mapOf(Map.Entry<?, ?>... entries) {
        return new LinkedHashMap<>(Stream.of(entries).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    static Pair<?, ?> pairOf(Object first, Object second) {
        return Pair.mutable(first, second);
    }

    static Triple<?, ?, ?> tripleOf(Object first, Object second, Object third) {
        return Triple.mutable(first, second, third);
    }

    static Object first(Object obj) {
        if (obj instanceof LinkedList<?> list)
            return list.isEmpty() ? null : list.getFirst();
        if (obj instanceof List<?> list)
            return list.isEmpty() ? null : list.get(0);
        if (obj instanceof SortedSet<?> set)
            return set.isEmpty() ? null : set.first();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0 ? null : Array.get(obj, 0);
        }
        if (obj instanceof Iterable<?> iterable)
            return Iterables.first(iterable);
        if (obj instanceof Iterator<?> iterator)
            return Iterators.first(iterator);
        if (obj instanceof Map<?, ?> map)
            return first(map.entrySet());
        return null;
    }

    static Object last(Object obj) {
        if (obj instanceof LinkedList<?> list)
            return list.isEmpty() ? null : list.getLast();
        if (obj instanceof List<?> list)
            return list.isEmpty() ? null : list.get(list.size() - 1);
        if (obj instanceof SortedSet<?> set)
            return set.isEmpty() ? null : set.last();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0 ? null : Array.get(obj, length - 1);
        }
        if (obj instanceof Iterable<?> iterable)
            return Iterables.last(iterable);
        if (obj instanceof Iterator<?> iterator)
            return Iterators.last(iterator);
        if (obj instanceof Map<?, ?> map)
            return last(map.entrySet());
        return null;
    }
}
