package com.vasnatech.katip.template.document;

import com.vasnatech.katip.template.renderer.Root;

public record Document(Tag root) {

    public static Document EMPTY = new Document(new Tag(0, Root.instance()));

    @Override
    public String toString() {
        return String.valueOf(root);
    }
}
