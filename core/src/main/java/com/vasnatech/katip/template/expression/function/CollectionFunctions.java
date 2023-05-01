package com.vasnatech.katip.template.expression.function;

public interface CollectionFunctions {


    class CollectionFunction extends AbstractPureFunction {

        protected CollectionFunction(String name, int minNumberOfParameters, int maxNumberOfParameters) {
            super(name, minNumberOfParameters, maxNumberOfParameters);
        }

        @Override
        public Object invoke(Object[] params) {
            return null;
        }

        @Override
        public Class<?> returnType() {
            return String.class;
        }

        @Override
        public Class<?> parameterType(int index) {
            if (index == 0)
                return Iterable.class;
            return Object.class;
        }
    }
}
