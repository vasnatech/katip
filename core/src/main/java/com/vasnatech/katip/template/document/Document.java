package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.renderer.Root;

import java.nio.file.Path;

public record Document(Tag root) {

    public static Document EMPTY = new Document(new Tag(Path.of("."),0, Root.instance()));

    @Override
    public String toString() {
        return String.valueOf(root);
    }
}
