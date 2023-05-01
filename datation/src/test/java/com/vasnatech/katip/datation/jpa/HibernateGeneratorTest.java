package com.vasnatech.katip.datation.jpa;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.schema.Schemas;
import com.vasnatech.datation.schema.validate.Validator;
import com.vasnatech.datation.schema.validate.ValidatorFactory;
import com.vasnatech.katip.template.Output;
import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.renderer.DocumentRenderer;
import com.vasnatech.katip.template.renderer.DocumentRendererFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HibernateGeneratorTest {

    @Test
    void generate() throws IOException {
        com.vasnatech.datation.schema.parse.Parser schemaParser = com.vasnatech.datation.schema.parse.ParserFactory.instance().create();
        Schemas schemas = schemaParser.parseAndNormalize(Resources.asInputStream("schema.json"));
        Validator schemaValidator = ValidatorFactory.instance().create();
        List<Validator.Result> resultList = schemaValidator.validate(schemas);
        assertThat(resultList).isEmpty();

        Project project = Project.from("./jpa/hibernate", "hibernate.katip", "hibernate-field-type.katip");

        DocumentRenderer renderer = DocumentRendererFactory.instance().create(Map.of());
        try (Output out = new Output("./target/generated-sources/katip")) {
            renderer.render(
                    project,
                    project.document("hibernate.katip"),
                    out,
                    Map.of(
                            "schemas", schemas,
                            "package", "com.vasnatech.sec"
                    )
            );
        }
    }
}
