package com.vasnatech.katip.template;

import com.vasnatech.katip.template.part.Tag;

public record Document(Tag root) {

    @Override
    public String toString() {
        return String.valueOf(root);
    }
}
