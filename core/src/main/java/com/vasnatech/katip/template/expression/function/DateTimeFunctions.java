package com.vasnatech.katip.template.expression.function;

import org.apache.commons.lang3.LocaleUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public interface DateTimeFunctions {

    Now NOW = new Now();

    class Now extends AbstractPureFunction {

        Now() {
            super("now", 0, 1);
        }
        @Override
        public Object invoke(Object[] params) {
            Instant now = Instant.now();
            if (params.length == 0) {
                return DateTimeFormatter.ISO_INSTANT.format(now);
            } else {
                return DateTimeFormatter.ISO_INSTANT.withLocale(LocaleUtils.toLocale(String.valueOf(params[1])));
            }
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
