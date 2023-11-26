package com.vasnatech.katip.datation.schema;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ddl.DDLModule;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.entity.EntityModule;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.katip.datation.schema.entity.EntitySchemaProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class EntitySchemaGeneratorTest {

    @Test
    void generate() throws IOException {
        Modules.add(DDLModule.instance());
        SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                Map.of(
                        "normalize", true,
                        "validate", true
                )
        );
        DDLSchemas ddlSchemas = schemaLoader.load(Resources.asInputStream("ddl-schema.json"));


        ProjectTemplates.add(EntitySchemaProjectTemplate.instance());
        ProjectTemplates.get("schema/entity")
                .builder()
                .outputRoot("./target/generated-sources/katip/datation-schemas")
                .parameter("schemas", ddlSchemas)
                .parameter("ddlSchema", "ddl-schema.json")
                .parameter("entitySchema", "entity-schema.json")
                .run();
    }

    @Test
    void parse() throws IOException {
        Modules.add(EntityModule.instance());
        SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                Map.of(
                        "normalize", true,
                        "validate", true
                )
        );
        InputStream in = Resources.asInputStream("entity-schema.json");
        EntitySchemas entitySchemas = schemaLoader.load(in);
        System.out.println(entitySchemas);

    }
}
