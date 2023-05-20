package com.vasnatech.katip.datation.schema.entity;

import com.vasnatech.katip.template.ProjectTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public final class EntitySchemaProjectTemplate extends ProjectTemplate.Wrapper {

    private static EntitySchemaProjectTemplate INSTANCE = null;

    public static EntitySchemaProjectTemplate instance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new EntitySchemaProjectTemplate();
        }
        return INSTANCE;
    }

    private EntitySchemaProjectTemplate() throws IOException {
        super(
                    ProjectTemplate.of(
                    "schema/entity",
                    Path.of("./schema/entity"),
                    Set.of(
                            Path.of("entity-schema.katip"),
                            Path.of("entity-field-type.katip")
                    ),
                    Set.of(
                            Path.of("entity-schema.katip")
                    )
            )
        );
    }
}
