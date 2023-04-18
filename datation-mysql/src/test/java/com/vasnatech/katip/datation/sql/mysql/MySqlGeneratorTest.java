package com.vasnatech.katip.datation.sql.mysql;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.schema.Schemas;
import com.vasnatech.datation.schema.validate.Validator;
import com.vasnatech.datation.schema.validate.ValidatorFactory;
import com.vasnatech.katip.template.Document;
import com.vasnatech.katip.template.renderer.Renderer;
import com.vasnatech.katip.template.renderer.RendererFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MySqlGeneratorTest {

    @Test
    void generate() throws IOException {
        com.vasnatech.datation.schema.parse.Parser schemaParser = com.vasnatech.datation.schema.parse.ParserFactory.instance().create();
        Schemas db = schemaParser.parseAndNormalize(Resources.asInputStream("schema.json"));
        Validator schemaValidator = ValidatorFactory.instance().create();
        List<Validator.Result> resultList = schemaValidator.validate(db);
        assertThat(resultList).isEmpty();

        com.vasnatech.katip.template.parser.Parser documentParser = com.vasnatech.katip.template.parser.ParserFactory.instance().create(Map.of());
        Document document = documentParser.parse(Resources.asString("mysql-template"));

        Renderer renderer = RendererFactory.instance().create(Map.of());
        StringWriter out = new StringWriter();
        renderer.render(
                document,
                out,
                Map.of(
                        "schema", db.getSchemas().get("sec"),
                        "mysql", MySql.instance()
                )
        );

        System.out.println(out);
    }
}
