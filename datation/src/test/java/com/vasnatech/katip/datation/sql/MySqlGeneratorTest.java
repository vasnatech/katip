package com.vasnatech.katip.datation.sql;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.schema.ddl.DDLSchemas;
import com.vasnatech.datation.schema.parse.ddl.DDLParser;
import com.vasnatech.datation.schema.parse.ddl.DDLParserFactory;
import com.vasnatech.datation.schema.validate.ValidationInfo;
import com.vasnatech.datation.schema.validate.ddl.DDLValidator;
import com.vasnatech.datation.schema.validate.ddl.DDLValidatorFactory;
import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.renderer.DocumentRenderer;
import com.vasnatech.katip.template.renderer.DocumentRendererFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MySqlGeneratorTest {

    @Test
    void generate() throws IOException {
        DDLParser schemaParser = DDLParserFactory.instance().create();
        DDLSchemas schemas = schemaParser.parseAndNormalize(Resources.asInputStream("ddl-schema.json"));
        DDLValidator schemaValidator = DDLValidatorFactory.instance().create();
        List<ValidationInfo> resultList = schemaValidator.validate(schemas);
        assertThat(resultList).isEmpty();

        Project project = Project.from("./sql/mysql", "mysql.katip", "mysql-column.katip");

        DocumentRenderer renderer = DocumentRendererFactory.instance().create(Map.of());
        try (Output out = new Output("./target/generated-sources/katip/sql-scripts")) {
            renderer.render(
                    project,
                    project.document("mysql.katip"),
                    out,
                    Map.of("schemas", schemas)
            );
        }
    }
}
