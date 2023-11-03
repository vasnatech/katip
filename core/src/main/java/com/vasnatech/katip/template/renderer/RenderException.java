package com.vasnatech.katip.template.renderer;

import java.nio.file.Path;

public class RenderException extends RuntimeException {
    final Path path;
    final int line;


    public RenderException(Path path, int line, String message) {
        super(message);
        this.path = path;
        this.line = line;
    }

    public RenderException(Path path, int line, String message, Exception cause) {
        super(message, cause);
        this.path = path;
        this.line = line;
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
