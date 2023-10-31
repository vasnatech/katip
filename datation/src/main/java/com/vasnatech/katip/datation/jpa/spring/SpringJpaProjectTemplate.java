package com.vasnatech.katip.datation.jpa.spring;

import com.vasnatech.katip.template.ProjectTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public final class SpringJpaProjectTemplate extends ProjectTemplate.Wrapper {

    private static SpringJpaProjectTemplate INSTANCE = null;

    public static SpringJpaProjectTemplate instance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new SpringJpaProjectTemplate();
        }
        return INSTANCE;
    }

    private SpringJpaProjectTemplate() throws IOException {
        super (
            ProjectTemplate.fromPaths(
                    "jpa/spring",
                    SpringJpaProjectTemplate.class,
                    Set.of(
                            Path.of("jpa-repository.katip"),
                            Path.of("jpa-repository-id-type.katip")
                    ),
                    Set.of(Path.of("jpa-repository.katip"))
            )
        );
    }
}
