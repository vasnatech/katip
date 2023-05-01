package com.vasnatech.katip.template.expression;

import com.vasnatech.katip.template.expression.Name;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}
