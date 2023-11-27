package com.vasnatech.katip.maven.plugin;

import java.util.Map;
import java.util.StringJoiner;

public class KatipConfig {
    String template;
    String out;
    Map<String, ?> parameters;
    Map<String, ?> renderConfigs;
    Map<String, SchemaConfig> datationSchemas;

    public static class SchemaConfig {
        String mediaType;
        boolean normalize;
        boolean validate;
        String path;

        @Override
        public String toString() {
            return new StringJoiner(", ", SchemaConfig.class.getSimpleName() + "[", "]")
                    .add("mediaType='" + mediaType + "'")
                    .add("normalize=" + normalize)
                    .add("validate=" + validate)
                    .add("path='" + path + "'")
                    .toString();
        }
    }
}
