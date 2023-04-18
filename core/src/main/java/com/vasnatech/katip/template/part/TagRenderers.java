package com.vasnatech.katip.template.part;

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
        register(new Get());
        register(new Set());

        register(new Root());
        register(new If());
        register(new Foreach());
    }
}
