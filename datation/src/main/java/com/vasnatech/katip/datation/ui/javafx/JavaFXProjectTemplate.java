package com.vasnatech.katip.datation.ui.javafx;

import com.vasnatech.katip.template.ProjectTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public final class JavaFXProjectTemplate extends ProjectTemplate.Wrapper {

    private static JavaFXProjectTemplate INSTANCE = null;

    public static JavaFXProjectTemplate instance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new JavaFXProjectTemplate();
        }
        return INSTANCE;
    }

    private JavaFXProjectTemplate() throws IOException {
        super (
            ProjectTemplate.fromPaths(
                    "ui/javafx",
                    JavaFXProjectTemplate.class,
                    Set.of(
                            Path.of("javafx-fxml--default-component-model.katip"),
                            Path.of("javafx-fxml-component-type--default-component-model.katip"),
                            Path.of("javafx-fxml-container--default-component-model.katip"),
                            Path.of("javafx-fxml-container-children--default-component-model.katip"),
                            Path.of("javafx-fxml-container-content--default-component-model.katip"),
                            Path.of("javafx-fxml-containerProperty-as-attribute--default-component-model.katip"),
                            Path.of("javafx-fxml-containerProperty-as-tag--default-component-model.katip"),
                            Path.of("javafx-fxml-control--default-component-model.katip"),
                            Path.of("javafx-fxml-property-as-attribute--default-component-model.katip"),
                            Path.of("javafx-fxml-property-as-tag--default-component-model.katip"),
                            Path.of("javafx-fxml-property-value-grow--default-component-model.katip"),
                            Path.of("javafx-fxml-property-value-alignment-horizontal--default-component-model.katip"),
                            Path.of("javafx-fxml-property-value-alignment-vertical--default-component-model.katip")
                    ),
                    Set.of(Path.of("javafx-fxml--default-component-model.katip"))
            )
        );
    }
}
