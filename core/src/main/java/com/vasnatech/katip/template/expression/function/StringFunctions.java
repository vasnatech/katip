package com.vasnatech.katip.template.expression.function;

import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StringFunctions {

    StringFunction TO_UPPER_CASE = new StringFunction("toUpperCase", 1, 2, params -> {
        Locale locale = params.length > 1 ? LocaleUtils.toLocale(params[0].toString()) : Locale.ENGLISH;
        return String.valueOf(params[0]).toUpperCase(locale);
    });
    StringFunction TO_LOWER_CASE = new StringFunction("toLowerCase", 1, 2, params -> {
        Locale locale = params.length > 1 ? LocaleUtils.toLocale(params[0].toString()) : Locale.ENGLISH;
        return String.valueOf(params[0]).toLowerCase(locale);
    });
    StringFunction CONCAT = new StringFunction("concat", 2, Integer.MAX_VALUE, params -> Stream.of(params).map(String::valueOf).collect(Collectors.joining()));
    StringFunction REPLACE = new StringFunction("replace", 3, 3, params -> String.valueOf(params[0]).replace(String.valueOf(params[1]), String.valueOf(params[2])));

    class StringFunction extends AbstractPureFunction {
        Invokable invokable;

        StringFunction(String name, int minNumberOfParameters, int maxNumberOfParameters, Invokable invokable) {
            super(name, minNumberOfParameters, maxNumberOfParameters);
            this.invokable = invokable;
        }
        @Override
        public Object invoke(Object[] params) {
            return invokable.invoke(params);
        }
        @Override
        public Class<?> returnType() {
            return String.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return String.class;
        }
    }

}
