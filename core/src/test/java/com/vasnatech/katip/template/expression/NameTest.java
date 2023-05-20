package com.vasnatech.katip.template.expression;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class NameTest {

    @Test
    void toSnakeCase() {
        Name name = new Name(List.of("testing", "snake", "case"));
        String expected = "testing_snake_case";

        String actual = name.toSnakeCase();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fromSnakeCase() {
        String source = "TestIng_snake_CASE";
        Name expected = new Name(List.of("testing", "snake", "case"));

        Name actual = Name.fromSnakeCase(source);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toKebabCase() {
        Name name = new Name(List.of("testing", "kebab", "case"));
        String expected = "testing-kebab-case";

        String actual = name.toKebabCase();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fromKebabCase() {
        String source = "TestIng-kebab-CASE";
        Name expected = new Name(List.of("testing", "kebab", "case"));

        Name actual = Name.fromKebabCase(source);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toPascalCase() {
        Name name = new Name(List.of("testing", "pascal", "case"));
        String expected = "TestingPascalCase";

        String actual = name.toPascalCase();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fromPascalCase() {
        String source = "TestingPascalCase";
        Name expected = new Name(List.of("testing", "pascal", "case"));

        Name actual = Name.fromPascalCase(source);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toCamelCase() {
        Name name = new Name(List.of("testing", "camel", "case"));
        String expected = "testingCamelCase";

        String actual = name.toCamelCase();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fromCamelCase() {
        String source = "testingCamelCase";
        Name expected = new Name(List.of("testing", "camel", "case"));

        Name actual = Name.fromCamelCase(source);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void length() {
        List<String> words = IntStream.range(0, RandomUtils.nextInt(1, 25))
                .map(index -> RandomUtils.nextInt(0, 15))
                .mapToObj(RandomStringUtils::randomAlphabetic)
                .collect(Collectors.toList());

        Name name = new Name(words);
        int expected = name.getWords().stream().map(String::length).reduce(Integer::sum).orElse(0);

        int actual = name.length();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("dataCharAt")
    void charAt(Name name, int index, char expected) {
        char actual = name.charAt(index);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> dataCharAt() {
        Name name1 = new Name(List.of("abcd", "efg", "h"));
        Name name2 = new Name(List.of("abcd", "efg", "", "", "h"));
        return Stream.of(
                Arguments.of(name1, 0, "a"),
                Arguments.of(name1, 1, "b"),
                Arguments.of(name1, 2, "c"),
                Arguments.of(name1, 3, "d"),
                Arguments.of(name1, 4, "e"),
                Arguments.of(name1, 5, "f"),
                Arguments.of(name1, 6, "g"),
                Arguments.of(name1, 7, "h"),

                Arguments.of(name2, 7, "h")
        );
    }

    @ParameterizedTest
    @MethodSource("dataSubSequence")
    void subSequence(Name name, int start, int end, CharSequence expected) {
        CharSequence actual = name.subSequence(start, end);
        assertThat(actual).startsWith(expected).endsWith(expected);
    }

    static Stream<Arguments> dataSubSequence() {
        Name name1 = new Name(List.of("abcd", "efg", "h"));
        Name name2 = new Name(List.of("abcd", "efg", "", "", "h"));
        return Stream.of(
                Arguments.of(name1, 0, 2, "ab"),
                Arguments.of(name1, 0, 4, "abcd"),
                Arguments.of(name1, 1, 4, "bcd"),
                Arguments.of(name1, 2, 5, "cde"),
                Arguments.of(name1, 3, 7, "defg"),
                Arguments.of(name1, 3, 8, "defgh"),

                Arguments.of(name2, 3, 8, "defgh"),
                Arguments.of(name2, 7, 8, "h")
        );
    }
}
