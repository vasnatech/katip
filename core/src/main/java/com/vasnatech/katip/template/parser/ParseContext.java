package com.vasnatech.katip.template.parser;

import java.nio.file.Path;

public class ParseContext {

    protected final Path path;
    protected final CharSequence content;

    protected int line = 1;

    public ParseContext(Path path, CharSequence content) {
        this.path = path;
        this.content = content;
    }

    public Path getPath() {
        return path;
    }

    public CharSequence getContent() {
        return content;
    }

    public int getLine() {
        return line;
    }

    public void increaseLine() {
        ++line;
    }

    public int increaseAndGetLine() {
        return ++line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public static ParseContext of(Path path, CharSequence content) {
        return new ParseContext(path, content);
    }
}
