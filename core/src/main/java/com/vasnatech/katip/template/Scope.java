package com.vasnatech.katip.template;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {

    final Scope parent;
    final Map<String, Object> variables;

    public Scope(Map<String, Object> variables) {
        this.parent = null;
        this.variables = new LinkedHashMap<>(variables);
    }

    public Scope(Scope parent) {
        this.parent = parent;
        this.variables = new LinkedHashMap<>();
    }

    public boolean containsKey(String name) {
        Scope scope = this;
        while (scope != null) {
            if (scope.variables.containsKey(name)) {
                return true;
            }
            scope = scope.parent;
        }
        return false;
    }

    public Object get(String name) {
        Scope scope = this;
        while (scope != null) {
            if (scope.variables.containsKey(name)) {
                return scope.variables.get(name);
            }
            scope = scope.parent;
        }
        return null;
    }

    public Object put(String name, Object value) {
        Scope scope = this;
        while (scope != null) {
            if (scope.variables.containsKey(name)) {
                return scope.variables.put(name, value);
            }
            scope = scope.parent;
        }
        return variables.put(name, value);
    }
}
