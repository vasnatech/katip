package com.vasnatech.katip.maven.plugin;

import com.vasnatech.commons.reflection.ReflectionUtil;
import com.vasnatech.commons.serialize.MediaTypes;
import com.vasnatech.datation.Module;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import com.vasnatech.datation.load.SchemaLoaderFactory;
import com.vasnatech.datation.schema.Schema;
import com.vasnatech.katip.template.ProjectTemplate;
import com.vasnatech.katip.template.ProjectTemplates;
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

@Mojo(name = "katip", defaultPhase = LifecyclePhase.COMPILE)
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

        getLog().info("[KATIP] Initializing plugin");
        for (String module : modules) {
            getLog().info("[KATIP] Registering datation module " + module);
            registerDatationModule(classLoader, module);
        }

        Map<String, Schema> schemaMap = new HashMap<>();
        for (Map.Entry<String, KatipConfig.SchemaConfig> e : project.datationSchemas.entrySet()) {
            getLog().info("[KATIP] Loading datation schema " + e.getKey());
            schemaMap.put(e.getKey(), loadDatationSchema(e.getValue()));
        }

        getLog().info("[KATIP] Initializing template " + project.template);
        loadProjectTemplate(classLoader, project.template);

        getLog().info("[KATIP] Running template " + project.template);
        ProjectTemplates.get(project.template)
                .builder()
                .outputRoot(project.out)
                .parameters(project.parameters)
                .parameters(schemaMap)
                .run();
    }

    void registerDatationModule(ClassLoader classLoader, String moduleName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, MojoExecutionException {
        String className = switch (moduleName) {
            case "ddl" -> "com.vasnatech.datation.ddl.DDLModule";
            case "entity" -> "com.vasnatech.datation.entity.EntityModule";
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
            default -> throw new MojoExecutionException("Unsupported template " + templateName);
        };
        Class<?> clazz = classLoader.loadClass(className);
        ProjectTemplate projectTemplate = (ProjectTemplate) ReflectionUtil.invokeStaticMethod(clazz, "instance");
        ProjectTemplates.add(projectTemplate);
    }
}
