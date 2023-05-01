package com.vasnatech.katip.template.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Name {

    List<String> words;

    Name(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
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

    public String toSnakeCase() {
        return String.join("_", words);
    }

    public String toKebabCase() {
        return String.join("-", words);
    }

    public String toPascalCase() {
        return words.stream()
                .map(this::capitalizeFirstLetter)
                .collect(Collectors.joining());
    }

    public String toCamelCase() {
        if (words.size() < 1)
            return String.join("", words);
        return words.get(0) + words.subList(1, words.size()).stream()
                .map(this::capitalizeFirstLetter)
                .collect(Collectors.joining());
    }

    String capitalizeFirstLetter(String source) {
        return Character.toUpperCase(source.charAt(0)) + source.substring(1);
    }


    public static Name fromSnakeCase(String source) {
        return new Name(Stream.of(source.split("_")).map(String::toLowerCase).collect(Collectors.toList()));
    }

    public static Name fromKebabCase(String source) {
        return new Name(Stream.of(source.split("-")).map(String::toLowerCase).collect(Collectors.toList()));
    }

    public static Name fromPascalCase(String source) {
        int from = 0;
        int to = 0;
        List<String> words = new ArrayList<>();
        while (to < source.length()) {
            if (Character.isUpperCase(source.charAt(to))) {
                if (from < to) {
                    words.add(source.substring(from, to).toLowerCase());
                }
                from = to;
            }
            ++to;
        }
        if (from < to) {
            words.add(source.substring(from, to).toLowerCase());
        }
        return new Name(words);
    }

    public static Name fromCamelCase(String source) {
        return fromPascalCase(source);
    }
}
