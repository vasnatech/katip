package com.vasnatech.katip.template.expression.function;

import com.vasnatech.katip.template.expression.Name;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface NamingFunctions {

    NameToString TO_SNAKE_CASE = new NameToString("toSnakeCase", Name::toSnakeCase);
    StringToName FROM_SNAKE_CASE = new StringToName("fromSnakeCase", Name::fromSnakeCase);
    NameToString TO_KEBAB_CASE = new NameToString("toKebabCase", Name::toKebabCase);
    StringToName FROM_KEBAB_CASE = new StringToName("fromKebabCase", Name::fromKebabCase);
    NameToString TO_PASCAL_CASE = new NameToString("toPascalCase", Name::toPascalCase);
    StringToName FROM_PASCAL_CASE = new StringToName("fromPascalCase", Name::fromPascalCase);
    NameToString TO_CAMEL_CASE = new NameToString("toCamelCase", Name::toCamelCase);
    StringToName FROM_CAMEL_CASE = new StringToName("fromCamelCase", Name::fromCamelCase);

    class StringToName extends AbstractPureFunction {
        Function<String, Name> function;
        StringToName(String name, Function<String, Name> function) {
            super(name, 1, 1);
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            return function.apply(String.valueOf(params[0]));
        }
        @Override
        public Class<?> returnType() {
            return Name.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return String.class;
        }
    }


    class NameToString extends AbstractPureFunction {
        BiFunction<Name, Locale, String> function;

        NameToString(String name, BiFunction<Name, Locale, String> function) {
            super(name, 1, 2);
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            Locale locale = params.length > 1 ? LocaleUtils.toLocale(params[1].toString()) : Locale.ENGLISH;
            return function.apply((Name)params[0], locale);
        }
        @Override
        public Class<?> returnType() {
            return String.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return switch (index) {
                case 0 -> Name.class;
                case 1 -> String.class;
                default -> Object.class;
            };
        }
    }
}
