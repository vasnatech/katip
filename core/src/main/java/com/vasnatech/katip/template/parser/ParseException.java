package com.vasnatech.katip.template.parser;

import java.nio.file.Path;

public class ParseException extends RuntimeException {
    final Path path;
    final int line;


    public ParseException(Path path, int line, String message) {
        super(message);
        this.path = path;
        this.line = line;
    }

    public ParseException(Path path, int line, String message, Exception cause) {
        super(message, cause);
        this.path = path;
        this.line = line;
    }

    public ParseException(ParseContext parseContext, String message) {
        this(parseContext.getPath(), parseContext.getLine(), message);
    }

    public ParseException(ParseContext parseContext, String message, Exception cause) {
        this(parseContext.getPath(), parseContext.getLine(), message, cause);
    }

    public Path getPath() {
        return path;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " at " + path + ":" + line;
    }
}
