package com.vasnatech.katip.datation.ui;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.design.UIDesignModule;
import com.vasnatech.katip.datation.ui.javafx.JavaFXProjectTemplate;
import com.vasnatech.katip.template.Project;
import com.vasnatech.katip.template.ProjectTemplates;
import com.vasnatech.katip.template.log.Log;
import com.vasnatech.katip.template.log.SystemOutLogger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class JavaFXGeneratorTest {

    @Test
    void generate() throws IOException {
        Modules.add(UIDesignModule.instance());
        SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                Map.of(
                        "normalize", true,
                        "validate", true
                )
        );

        Log.logger(SystemOutLogger.instance());

        ProjectTemplates.add(JavaFXProjectTemplate.instance());
        Project.Builder builder = ProjectTemplates.get("ui/javafx")
                .builder()
                .outputRoot("./target/generated-sources/katip/fxml")
                .renderConfig("debugEnabled", false);

        builder.parameter("schema", schemaLoader.load(Resources.asInputStream("ui/contact--ui-design.json"))).parameter("path", "contact.fxml").run();
        builder.parameter("schema", schemaLoader.load(Resources.asInputStream("ui/contact-02--ui-design.json"))).parameter("path", "contact-02.fxml").run();
        builder.parameter("schema", schemaLoader.load(Resources.asInputStream("ui/contact-list--ui-design.json"))).parameter("path", "contact-list.fxml").run();
        builder.parameter("schema", schemaLoader.load(Resources.asInputStream("ui/contact-list-02--ui-design.json"))).parameter("path", "contact-list--02.fxml").run();
        builder.parameter("schema", schemaLoader.load(Resources.asInputStream("ui/test-01--ui-design.json"))).parameter("path", "test-01.fxml").run();
    }
}
