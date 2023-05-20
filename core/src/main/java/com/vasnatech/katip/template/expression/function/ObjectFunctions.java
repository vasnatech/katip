package com.vasnatech.katip.template.expression.function;

import java.util.function.Function;

public interface ObjectFunctions {

    ObjectFunction<Boolean> IS_PRESENT = new ObjectFunction<>("isPresent", Boolean.class, params -> params[0] != null);

    ObjectFunction<Boolean> IS_NOT_PRESENT = new ObjectFunction<>("isNotPresent", Boolean.class, params -> params[0] == null);

    class ObjectFunction<R> extends AbstractPureFunction {
        Class<R> returnType;
        Function<Object[], R> function;

        ObjectFunction(String name, Class<R> returnType, Function<Object[], R> function) {
            super(name, 1, 1);
            this.returnType = returnType;
            this.function = function;
        }
        @Override
        public R invoke(Object[] params) {
            return function.apply(params);
        }
        @Override
        public Class<?> returnType() {
            return returnType;
        }
        @Override
        public Class<?> parameterType(int index) {
            return String.class;
        }
    }

}
