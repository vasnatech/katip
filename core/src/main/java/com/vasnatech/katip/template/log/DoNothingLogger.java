package com.vasnatech.katip.template.log;

public final class DoNothingLogger implements Logger {

    static DoNothingLogger INSTANCE = new DoNothingLogger();

    public static DoNothingLogger instance() {
        return INSTANCE;
    }

    private DoNothingLogger() {
    }

    @Override
    public void debug(CharSequence message) {
    }

    @Override
    public void info(CharSequence message) {
    }

    @Override
    public void warn(CharSequence message) {
    }

    @Override
    public void error(CharSequence message) {
    }
}
