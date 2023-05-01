package com.vasnatech.katip.template.expression.function;

import com.vasnatech.katip.template.expression.Name;

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
        Function<Name, String> function;

        NameToString(String name, Function<Name, String> function) {
            super(name, 1, 1);
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            return function.apply((Name)params[0]);
        }
        @Override
        public Class<?> returnType() {
            return String.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return Name.class;
        }
    }
}
