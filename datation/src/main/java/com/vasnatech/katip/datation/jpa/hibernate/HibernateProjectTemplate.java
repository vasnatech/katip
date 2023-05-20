package com.vasnatech.katip.datation.jpa.hibernate;

import com.vasnatech.katip.template.ProjectTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public final class HibernateProjectTemplate extends ProjectTemplate.Wrapper {

    private static HibernateProjectTemplate INSTANCE = null;

    public static HibernateProjectTemplate instance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new HibernateProjectTemplate();
        }
        return INSTANCE;
    }

    private HibernateProjectTemplate() throws IOException {
        super (
                ProjectTemplate.of(
                    "jpa/hibernate",
                    Path.of("./jpa/hibernate"),
                    Set.of(
                            Path.of("hibernate.katip"),
                            Path.of("hibernate-field-type.katip"),
                            Path.of("hibernate-cfg-xml.katip")
                    ),
                    Set.of(
                            Path.of("hibernate.katip")
                    )
            )
        );
    }
}
