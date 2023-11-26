package com.vasnatech.katip.template.log;

public interface Logger {

    void debug(CharSequence message);
    void info(CharSequence message);
    void warn(CharSequence message);
    void error(CharSequence message);
}
