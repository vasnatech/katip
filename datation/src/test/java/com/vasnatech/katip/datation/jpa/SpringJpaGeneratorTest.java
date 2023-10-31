package com.vasnatech.katip.datation.jpa;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.entity.EntityModule;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import com.vasnatech.katip.datation.jpa.hibernate.HibernateProjectTemplate;
import com.vasnatech.katip.datation.jpa.spring.SpringJpaProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class SpringJpaGeneratorTest {

    @Test
    void generate() throws IOException {
        Modules.add(EntityModule.instance());
        SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                Map.of(
                        "normalize", true,
                        "validate", true
                )
        );
        EntitySchemas schemas = schemaLoader.load(Resources.asInputStream("entity-schema.json"));


        ProjectTemplates.add(SpringJpaProjectTemplate.instance());
        ProjectTemplates.get("jpa/spring")
                .builder()
                .outputRoot("./target/generated-sources/katip")
                .parameter("schemas", schemas)
                .parameter("entityPackage", "com.vasnatech.entity")
                .parameter("package", "com.vasnatech.repository")
                .run();
    }
}
