package com.vasnatech.katip.template.expression.function;

import com.vasnatech.katip.template.expression.Name;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StringFunctions {

    StringFunction<Boolean> IS_EMPTY = new StringFunction<>("isEmpty", 1, 1, Boolean.class, params -> params[0] == null || String.valueOf(params[0]).isEmpty());
    StringFunction<Boolean> IS_NOT_EMPTY = new StringFunction<>("isNotEmpty", 1, 1, Boolean.class, params -> params[0] != null && !String.valueOf(params[0]).isEmpty());
    StringFunction<String> TO_UPPER_CASE = new StringFunction<>("toUpperCase", 1, 2, String.class,
            params -> {
                Locale locale = params.length > 1 ? LocaleUtils.toLocale(params[1].toString()) : Locale.ENGLISH;
                if (params[0] instanceof String str)
                    return str.toUpperCase(locale);
                if (params[0] instanceof Name name)
                    return name.toUpperCase(locale);
                return String.valueOf(params[0]).toUpperCase(locale);
            }
    );
    StringFunction<String> TO_LOWER_CASE = new StringFunction<>("toLowerCase", 1, 2, String.class,
            params -> {
                Locale locale = params.length > 1 ? LocaleUtils.toLocale(params[1].toString()) : Locale.ENGLISH;
                if (params[0] instanceof String str)
                    return str.toLowerCase(locale);
                if (params[0] instanceof Name name)
                    return name.toLowerCase(locale);
                return String.valueOf(params[0]).toLowerCase(locale);
            }
    );
    StringFunction<String> CONCAT = new StringFunction<>("concat", 2, Integer.MAX_VALUE, String.class, params -> Stream.of(params).map(String::valueOf).collect(Collectors.joining()));
    StringFunction<String> REPLACE = new StringFunction<>("replace", 3, 3, String.class, params -> String.valueOf(params[0]).replace(String.valueOf(params[1]), String.valueOf(params[2])));

    class StringFunction<R> extends AbstractPureFunction {
        Class<R> returnType;
        Function<Object[], R> function;

        StringFunction(String name, int minNumberOfParameters, int maxNumberOfParameters, Class<R> returnType, Function<Object[], R> function) {
            super(name, minNumberOfParameters, maxNumberOfParameters);
            this.returnType = returnType;
            this.function = function;
        }
        @Override
        public Object invoke(Object[] params) {
            return function.apply(params);
        }
        @Override
        public Class<?> returnType() {
            return returnType;
        }
        @Override
        public Class<?> parameterType(int index) {
            return CharSequence.class;
        }
    }

}
