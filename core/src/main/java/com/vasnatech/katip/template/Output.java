package com.vasnatech.katip.template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Output implements Appendable, Closeable {

    Path root;
    Path path;
    Writer writer;

    public Output(String root) throws IOException {
        this(Path.of(root));
    }

    public Output(Path root) throws IOException {
        this(root, (Path)null);
    }

    public Output(Path root, Path relativePath) throws IOException {
        this.root = root;
        if (relativePath != null) {
            this.path = root.resolve(relativePath);
            Files.createDirectories(this.path.getParent());
            OutputStream out = Files.newOutputStream(this.path);
            this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        } else {
            this.path = null;
            this.writer = null;
        }
    }

    public Output(Path root, Writer writer) throws IOException {
        this.root = root;
        this.path = null;
        this.writer = writer;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        if (writer != null)
            writer.append(csq);
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        if (writer != null)
            writer.append(csq, start, end);
        return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
        if (writer != null)
            writer.append(c);
        return this;
    }

    @Override
    public void close() throws IOException {
        if (writer != null)
            writer.close();
    }

    public Output createFile(String pathAsString) throws IOException {
        return new Output(root, Path.of(pathAsString));
    }

    public Output create(Writer writer) throws IOException {
        return new Output(root, writer);
    }
}
