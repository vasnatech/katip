package com.vasnatech.katip.template.expression.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

public interface CreateFunctions {

    CreateFunction CREATE_SET = new CreateFunction("createSet", LinkedHashSet::new);
    CreateFunction CREATE_LIST = new CreateFunction("createList", ArrayList::new);
    CreateFunction CREATE_MAP = new CreateFunction("createMap", LinkedHashMap::new);

    class CreateFunction extends AbstractPureFunction {

        Supplier<Object> creator;

        CreateFunction(String name, Supplier<Object> creator) {
            super(name, 0, 1);
            this.creator = creator;
        }
        @Override
        public Object invoke(Object[] params) {
            return creator.get();
        }
        @Override
        public Class<?> returnType() {
            return Object.class;
        }
        @Override
        public Class<?> parameterType(int index) {
            return Object.class;
        }
    }

}
