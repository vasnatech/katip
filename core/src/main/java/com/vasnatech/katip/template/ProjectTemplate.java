package com.vasnatech.katip.template;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.parser.Parser;
import com.vasnatech.katip.template.parser.ParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public interface ProjectTemplate {

    String name();

    Path root();

    Map<Path, Document> documents();

    Set<Path> renderPaths();


    default Project.Builder builder() {
        return append(new Project.Builder());
    }

    default Project.Builder append(Project.Builder builder) {
        return builder
                .root(this.root())
                .documents(this.documents())
                .renders(this.renderPaths());
    }

    static ProjectTemplate of(String name, Path root, Set<Path> templatePaths, Set<Path> renderPaths) throws IOException {
        Parser parser = ParserFactory.instance().create(Map.of());
        Map<Path, Document> documents = new LinkedHashMap<>();
        for (Path documentPath : templatePaths) {
            if (documents.containsKey(documentPath))
                continue;
            Path path = root.resolve(documentPath);
            try (Reader reader = new InputStreamReader(Resources.asInputStream(path.toString()))) {
                Document document = parser.parse(reader);
                documents.put(documentPath, document);
            }
        }
        return of(name, root, documents, renderPaths);
    }

    static ProjectTemplate of(String name, Path root, Map<Path, Document> documents, Set<Path> renderPaths) {
        return new Default(name, root, documents, renderPaths);
    }

    record Default(String name, Path root, Map<Path, Document> documents, Set<Path> renderPaths) implements ProjectTemplate {}

    class Wrapper implements ProjectTemplate {
        final ProjectTemplate source;

        public Wrapper(ProjectTemplate source) {
            this.source = source;
        }
        @Override
        public String name() {
            return source.name();
        }
        @Override
        public Path root() {
            return source.root();
        }
        @Override
        public Map<Path, Document> documents() {
            return source.documents();
        }
        @Override
        public Set<Path> renderPaths() {
            return source.renderPaths();
        }
    }
}
