package com.vasnatech.katip.template.expression.function;

import com.vasnatech.commons.collection.Streams;

import java.util.stream.Collectors;

public final class Join extends AbstractPureFunction {

    public static final Join INSTANCE = new Join();

    private Join() {
        super("join", 2, 4);
    }

    @Override
    public Object invoke(Object[] params) {
        Iterable<?> iterable = (Iterable<?>) params[0];
        String deliminator = String.valueOf(params[1]);
        String prefix = params.length > 2 ? String.valueOf(params[2]) : "";
        String suffix = params.length > 3 ? String.valueOf(params[3]) : "";
        return Streams.from(iterable).map(String::valueOf).collect(Collectors.joining(deliminator, prefix, suffix));
    }

    @Override
    public Class<?> returnType() {
        return String.class;
    }

    @Override
    public Class<?> parameterType(int index) {
        if (index == 0)
            return Iterable.class;
        return String.class;
    }
}
