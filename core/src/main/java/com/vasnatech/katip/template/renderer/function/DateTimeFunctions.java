package com.vasnatech.katip.template.renderer.function;

import java.time.Instant;

public interface DateTimeFunctions {

    public static Instant now() {
        return Instant.now();
    }
}
