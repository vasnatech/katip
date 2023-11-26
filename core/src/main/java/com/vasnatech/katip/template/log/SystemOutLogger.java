package com.vasnatech.katip.template.log;

public final class SystemOutLogger extends PrintStreamLogger {

    static SystemOutLogger INSTANCE = new SystemOutLogger();

    public static SystemOutLogger instance() {
        return INSTANCE;
    }

    private SystemOutLogger() {
        super(System.out);
    }
}
