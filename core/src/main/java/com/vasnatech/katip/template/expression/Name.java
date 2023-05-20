package com.vasnatech.katip.template.expression;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Name implements CharSequence {

    List<String> words;

    Name(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    @Override
    public int length() {
        return words.stream().map(String::length).reduce(Integer::sum).orElse(0);
    }

    @Override
    public char charAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index);
        }
        int ind = index;
        for (String word : words) {
            if (ind < word.length())
                return word.charAt(ind);
            ind -= word.length();
        }
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end < 0) {
            throw new IndexOutOfBoundsException("start or end can not be negative. start: " + start + " end: " + end);
        }
        int size = end - start;
        if (size < 0) {
            throw new IndexOutOfBoundsException("start can not be greater than end. start: " + start + " end: " + end);
        }
        StringBuilder builder = new StringBuilder(size);
        int s = start;
        int e = end;
        Iterator<String> iterator = words.iterator();
        while (iterator.hasNext() && e > 0) {
            String word = iterator.next();
            if (s >= word.length()) {
                s -= word.length();
                e -= word.length();
                continue;
            }
            if (e < word.length()) {
                builder.append(word, s, e);
                e = 0;
                break;
            }
            builder.append(word, s, word.length());
            s = 0;
            e -= (word.length() - s);
        }
        if (e > 0) {
            throw new IndexOutOfBoundsException("end can not be greater than the length. start: " + start + " end: " + end);
        }
        return builder;
    }

    @Override
    public String toString() {
        return String.join(" ", words);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(words, name.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(words);
    }

    public String toUpperCase() {
        return toUpperCase(Locale.ENGLISH);
    }

    public String toUpperCase(Locale locale) {
        return words.stream()
                .map(it -> it.toUpperCase(locale))
                .collect(Collectors.joining());
    }

    public String toLowerCase() {
        return toLowerCase(Locale.ENGLISH);
    }

    public String toLowerCase(Locale locale) {
        return words.stream()
                .map(it -> it.toLowerCase(locale))
                .collect(Collectors.joining());
    }

    public String toSnakeCase() {
        return toSnakeCase(Locale.ENGLISH);
    }

    public String toSnakeCase(Locale locale) {
        return String.join("_", words);
    }

    public String toKebabCase() {
        return toKebabCase(Locale.ENGLISH);
    }

    public String toKebabCase(Locale locale) {
        return String.join("-", words);
    }

    public String toPascalCase() {
        return toPascalCase(Locale.ENGLISH);
    }

    public String toPascalCase(Locale locale) {
        return words.stream()
                .map(it -> capitalizeFirstLetter(it, locale))
                .collect(Collectors.joining());
    }

    public String toCamelCase() {
        return toCamelCase(Locale.ENGLISH);
    }

    public String toCamelCase(Locale locale) {
        if (words.size() < 1)
            return String.join("", words);
        return words.get(0) + words.subList(1, words.size()).stream()
                .map(it -> capitalizeFirstLetter(it, locale))
                .collect(Collectors.joining());
    }

    String capitalizeFirstLetter(String source, Locale locale) {
        return String.valueOf(source.charAt(0)).toUpperCase(locale) + source.substring(1);
    }


    public static Name fromSnakeCase(String source) {
        return fromSnakeCase(source, Locale.ENGLISH);
    }

    public static Name fromSnakeCase(String source, Locale locale) {
        return new Name(Stream.of(source.split("_")).map(it -> it.toLowerCase(locale)).collect(Collectors.toList()));
    }

    public static Name fromKebabCase(String source) {
        return fromKebabCase(source, Locale.ENGLISH);
    }

    public static Name fromKebabCase(String source, Locale locale) {
        return new Name(Stream.of(source.split("-")).map(it -> it.toLowerCase(locale)).collect(Collectors.toList()));
    }

    public static Name fromPascalCase(String source) {
        return fromPascalCase(source, Locale.ENGLISH);
    }

    public static Name fromPascalCase(String source, Locale locale) {
        int from = 0;
        int to = 0;
        List<String> words = new ArrayList<>();
        while (to < source.length()) {
            if (Character.isUpperCase(source.charAt(to))) {
                if (from < to) {
                    words.add(source.substring(from, to).toLowerCase(locale));
                }
                from = to;
            }
            ++to;
        }
        if (from < to) {
            words.add(source.substring(from, to).toLowerCase(locale));
        }
        return new Name(words);
    }

    public static Name fromCamelCase(String source) {
        return fromCamelCase(source, Locale.ENGLISH);
    }

    public static Name fromCamelCase(String source, Locale locale) {
        return fromPascalCase(source, locale);
    }
}
