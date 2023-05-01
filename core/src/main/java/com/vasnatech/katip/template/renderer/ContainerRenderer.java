package com.vasnatech.katip.template.renderer;

public abstract class ContainerRenderer implements TagRenderer {

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String toString() {
        return name();
    }
}
