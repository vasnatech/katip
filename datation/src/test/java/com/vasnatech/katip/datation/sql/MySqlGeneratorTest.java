package com.vasnatech.katip.datation.sql;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ddl.DDLModule;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
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
                .parameter("dropTable", true)
                .run();
    }
}
