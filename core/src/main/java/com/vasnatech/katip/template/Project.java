package com.vasnatech.katip.template;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.parser.Parser;
import com.vasnatech.katip.template.parser.ParserFactory;
import com.vasnatech.katip.template.renderer.DocumentRenderer;
import com.vasnatech.katip.template.renderer.DocumentRendererFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Project(
        Path root,
        Map<Path, Document> documents,
        Set<Document> renderDocuments,
        Path outputRoot,
        Map<String, Object> parameters
) implements Runnable {

    public Document document(String path) {
        return document(Path.of(path));
    }

    public Document document(Path path) {
        return documents.get(path);
    }

    public void run() {
        DocumentRenderer renderer = DocumentRendererFactory.instance().create(Map.of());
        try (Output out = new Output(outputRoot)) {
            for (Document renderDocument : renderDocuments) {
                renderer.render(
                        this,
                        renderDocument,
                        out,
                        parameters
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        Path root;
        Map<Path, Document> documents = new LinkedHashMap<>();
        Set<Path> templatePaths = new LinkedHashSet<>();
        Set<Path> renderPaths = new LinkedHashSet<>();
        Path outputRoot;
        Map<String, Object> parameters = new LinkedHashMap<>();

        public Builder root(String root) {
            return root(Path.of(root));
        }
        public Builder root(Path root) {
            this.root = root;
            return this;
        }

        public Builder documents(Map<Path, Document> documents) {
            this.documents.putAll(documents);
            return this;
        }
        public Builder document(Path path, Document document) {
            this.documents.put(path, document);
            return this;
        }

        public Builder templates(String... paths) {
            return templates(Stream.of(paths).map(Path::of).collect(Collectors.toSet()));
        }
        public Builder templates(Set<Path> paths) {
            this.templatePaths.addAll(paths);
            return this;
        }
        public Builder template(String path) {
            return template(Path.of(path));
        }
        public Builder template(Path path) {
            this.templatePaths.add(path);
            return this;
        }

        public Builder renders(String... paths) {
            return renders(Stream.of(paths).map(Path::of).collect(Collectors.toSet()));
        }
        public Builder renders(Set<Path> paths) {
            this.renderPaths.addAll(paths);
            return this;
        }
        public Builder render(String path) {
            return render(Path.of(path));
        }
        public Builder render(Path path) {
            this.renderPaths.add(path);
            return this;
        }

        public Builder outputRoot(String outputRoot) {
            return outputRoot(Path.of(outputRoot));
        }
        public Builder outputRoot(Path outputRoot) {
            this.outputRoot = outputRoot;
            return this;
        }

        public Builder parameters(Map<String, ?> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }
        public Builder parameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }

        public Builder projectTemplate(String template) {
            return ProjectTemplates.get(template).append(this);
        }

        public Project build() throws IOException {
            Parser parser = ParserFactory.instance().create(Map.of());
            for (Path documentPath : templatePaths) {
                if (documents.containsKey(documentPath))
                    continue;
                Path path = root.resolve(documentPath);
                try (Reader reader = new InputStreamReader(Resources.asInputStream(path.toString()))) {
                    Document document = parser.parse(reader);
                    documents.put(documentPath, document);
                }
            }
            Set<Document> renderDocuments = renderPaths.stream().map(documents::get).collect(Collectors.toSet());
            return new Project(root, documents, renderDocuments, outputRoot, parameters);
        }

        public void run() throws IOException {
            build().run();
        }
    }
}
