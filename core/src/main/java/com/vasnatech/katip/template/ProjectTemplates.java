package com.vasnatech.katip.template;

import java.util.HashMap;
import java.util.Map;

public class ProjectTemplates {

    static final Map<String, ProjectTemplate> TEMPLATES = new HashMap<>();

    public static void add(ProjectTemplate template) {
        TEMPLATES.put(template.name(), template);
    }

    public static ProjectTemplate get(String name) {
        return TEMPLATES.get(name);
    }
}
