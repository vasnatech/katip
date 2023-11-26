package com.vasnatech.katip.template.renderer;

import java.util.HashMap;
import java.util.Map;

public final class TagRenderers {

    private TagRenderers() {
    }

    static final Map<String, TagRenderer> RENDERERS = new HashMap<>();

    public static TagRenderer get(String name) {
        return RENDERERS.get(name);
    }

    public static void register(TagRenderer renderer) {
        RENDERERS.put(renderer.name(), renderer);
    }

    static {
        register(Root.instance());

        register(new File());

        register(new Block());
        register(new Include());

        register(new Exe());
        register(new Get());
        register(new Set());
        register(new SetBlock());

        register(new If());
        register(new Unless());

        register(new Switch());
        register(new Case());
        register(new Else());

        register(new Foreach());
        register(new While());
    }
}
