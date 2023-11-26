package com.vasnatech.katip.template.log;

import java.io.PrintStream;

public class PrintStreamLogger implements Logger {

    protected PrintStream printStream;

    public PrintStreamLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void debug(CharSequence message) {
        printStream.print("DEBUG: ");
        printStream.println(message);
    }

    @Override
    public void info(CharSequence message) {
        printStream.print("INFO : ");
        printStream.println(message);
    }

    @Override
    public void warn(CharSequence message) {
        printStream.print("WARN : ");
        printStream.println(message);
    }

    @Override
    public void error(CharSequence message) {
        printStream.print("ERROR: ");
        printStream.println(message);
    }
}
