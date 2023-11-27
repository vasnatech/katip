package com.vasnatech.katip.maven.plugin;

import com.vasnatech.commons.reflection.ReflectionUtil;
import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.commons.schema.load.SchemaLoaderFactory;
import com.vasnatech.commons.schema.schema.Schema;
import com.vasnatech.commons.serialize.MediaTypes;
import com.vasnatech.katip.template.ProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
import com.vasnatech.katip.template.log.Log;
import com.vasnatech.katip.template.log.Logger;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Mojo(name = "katip", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Plugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    @Parameter(name = "modules")
    private String[] modules;

    @Parameter(name = "project")
    private KatipConfig project;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            runKatipProject();
        } catch (  ClassNotFoundException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | IllegalAccessException
                 | IOException
                 | DependencyResolutionRequiredException
                 | ArtifactResolutionException
                 | ArtifactNotFoundException
                 e
        ) {
            throw new MojoExecutionException(e);
        }
    }

    void runKatipProject() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, MojoExecutionException, DependencyResolutionRequiredException, ArtifactResolutionException, ArtifactNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        org.apache.maven.plugin.logging.Log pluginLogger = getLog();
        Log.logger(new MojoLogger(pluginLogger));

        Log.info("Initializing plugin");
        for (String module : modules) {
            Log.info("Registering datation module " + module);
            registerDatationModule(classLoader, module);
        }

        Map<String, Schema> schemaMap = new HashMap<>();
        for (Map.Entry<String, KatipConfig.SchemaConfig> e : project.datationSchemas.entrySet()) {
            Log.info("Loading datation schema " + e.getKey());
            schemaMap.put(e.getKey(), loadDatationSchema(e.getValue()));
        }

        Log.info("Initializing template " + project.template);
        loadProjectTemplate(classLoader, project.template);

        Log.info("Running template " + project.template);
        ProjectTemplates.get(project.template)
                .builder()
                .outputRoot(project.out)
                .parameters(project.parameters)
                .parameters(schemaMap)
                .renderConfig(project.renderConfigs)
                .run();
    }

    void registerDatationModule(ClassLoader classLoader, String moduleName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, MojoExecutionException {
        String className = switch (moduleName) {
            case "ddl" -> "com.vasnatech.datation.ddl.DDLModule";
            case "entity" -> "com.vasnatech.datation.entity.EntityModule";
            case "ui-design" -> "com.vasnatech.datation.ui.design.UIDesignModule";
            default -> throw new MojoExecutionException("Unsupported module " + moduleName);
        };
        Class<?> clazz = classLoader.loadClass(className);
        Module<?,?,?,?,?> module = (Module<?,?,?,?,?>) ReflectionUtil.invokeStaticMethod(clazz, "instance");
        Modules.add(module);
    }

    Schema loadDatationSchema(KatipConfig.SchemaConfig schemaConfig) throws IOException {
        SchemaLoaderFactory schemaLoaderFactory = SchemaLoaderFactories.get(MediaTypes.getByFileExtension(schemaConfig.mediaType));
        SchemaLoader schemaLoader = schemaLoaderFactory.create(
                Map.of(
                        "normalize", schemaConfig.normalize,
                        "validate", schemaConfig.validate
                )
        );
        try (InputStream in = new FileInputStream(schemaConfig.path)) {
            return schemaLoader.load(in);
        }
    }

    void loadProjectTemplate(ClassLoader classLoader, String templateName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, MojoExecutionException {
        String className = switch (templateName) {
            case "sql/mysql" -> "com.vasnatech.katip.datation.sql.mysql.MySqlProjectTemplate";
            case "schema/entity" -> "com.vasnatech.katip.datation.schema.entity.EntitySchemaProjectTemplate";
            case "jpa/hibernate" -> "com.vasnatech.katip.datation.jpa.hibernate.HibernateProjectTemplate";
            case "jpa/spring" -> "com.vasnatech.katip.datation.jpa.spring.SpringJpaProjectTemplate";
            case "ui/javafx" -> "com.vasnatech.katip.datation.ui.javafx.JavaFXProjectTemplate";
            default -> throw new MojoExecutionException("Unsupported template " + templateName);
        };
        Class<?> clazz = classLoader.loadClass(className);
        ProjectTemplate projectTemplate = (ProjectTemplate) ReflectionUtil.invokeStaticMethod(clazz, "instance");
        ProjectTemplates.add(projectTemplate);
    }

    static class MojoLogger implements Logger {

        final org.apache.maven.plugin.logging.Log log;

        MojoLogger(org.apache.maven.plugin.logging.Log log) {
            this.log = log;
        }

        @Override
        public void debug(CharSequence message) {
            log.info("[KATIP] " + message);
        }

        @Override
        public void info(CharSequence message) {
            log.info("[KATIP] " + message);
        }

        @Override
        public void warn(CharSequence message) {
            log.warn("[KATIP] " + message);
        }

        @Override
        public void error(CharSequence message) {
            log.error("[KATIP] " + message);
        }
    }
}
