package com.vasnatech.katip.datation.sql.mysql;

import com.vasnatech.katip.template.ProjectTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public final class MySqlProjectTemplate extends ProjectTemplate.Wrapper {

    private static MySqlProjectTemplate INSTANCE = null;

    public static MySqlProjectTemplate instance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new MySqlProjectTemplate();
        }
        return INSTANCE;
    }

    private MySqlProjectTemplate() throws IOException {
        super (
            ProjectTemplate.fromPaths(
                "sql/mysql",
                MySqlProjectTemplate.class,
                Set.of(Path.of("mysql.katip"), Path.of("mysql-column.katip")),
                Set.of(Path.of("mysql.katip"))
            )
        );
    }
}
