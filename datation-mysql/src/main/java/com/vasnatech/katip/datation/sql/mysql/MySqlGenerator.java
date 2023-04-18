package com.vasnatech.katip.datation.sql.mysql;

import com.vasnatech.datation.schema.Schemas;
import com.vasnatech.katip.datation.Generator;
import com.vasnatech.katip.datation.GeneratorException;
import com.vasnatech.katip.template.Document;
import com.vasnatech.katip.template.renderer.RendererFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class MySqlGenerator implements Generator {

    @Override
    public void generate(Map<String, ?> config) throws GeneratorException {
        Document document = (Document) config.get("document");
        Schemas db = (Schemas) config.get("db");
        try (StringWriter out = new StringWriter(1024)) {
            RendererFactory.instance().create(Map.of()).render(
                    document,
                    out,
                    Map.of(
                            "mysql", MySql.instance(),
                            "schema", db.getSchemas().get("sec")
                    )
            );
            System.out.println(out);
        } catch (IOException e) {
            throw new GeneratorException(e);
        }
    }
}
