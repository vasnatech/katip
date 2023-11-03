package com.vasnatech.katip.datation.jpa;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.entity.EntityModule;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import com.vasnatech.katip.datation.jpa.hibernate.HibernateProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class HibernateGeneratorTest {

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


        ProjectTemplates.add(HibernateProjectTemplate.instance());
        ProjectTemplates.get("jpa/hibernate")
                .builder()
                .outputRoot("./target/generated-sources/katip")
                .parameter("schemas", schemas)
                .parameter("package", "com.vasnatech.entity")
                .parameter("dialect", "org.hibernate.dialect.MariaDB103Dialect")
                .parameter("connectionUrl", "jdbc:mariadb://localhost:3306/dumrul")
                .parameter("connectionUsername", "developer")
                .parameter("connectionPassword", "developer")
                .parameter("connectionDriverClass", "org.mariadb.jdbc.Driver")
                .run();
    }
}
