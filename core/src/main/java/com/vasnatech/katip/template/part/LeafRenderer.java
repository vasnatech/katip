package com.vasnatech.katip.template.part;

public abstract class LeafRenderer implements TagRenderer {

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return name();
    }
}
