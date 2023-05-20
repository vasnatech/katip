package com.vasnatech.katip.datation.sql;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ddl.DDLModule;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import com.vasnatech.katip.datation.sql.mysql.MySqlProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class MySqlGeneratorTest {

    @Test
    void generate() throws IOException {
        Modules.add(DDLModule.instance());
        SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                Map.of(
                        "normalize", true,
                        "validate", true
                )
        );
        DDLSchemas schemas = schemaLoader.load(Resources.asInputStream("ddl-schema.json"));


        ProjectTemplates.add(MySqlProjectTemplate.instance());
        ProjectTemplates.get("sql/mysql")
                .builder()
                .outputRoot("./target/generated-sources/katip/sql-scripts")
                .parameter("schemas", schemas)
                .run();
    }
}
