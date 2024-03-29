package com.vasnatech.katip.template;

import com.vasnatech.commons.collection.Maps;
import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.text.ReaderCharSequence;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.parser.ParseContext;
import com.vasnatech.katip.template.parser.Parser;
import com.vasnatech.katip.template.parser.ParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ProjectTemplate {

    String name();

    Map<Path, Document> documents();

    Set<Path> renderPaths();


    default Project.Builder builder() {
        return append(new Project.Builder());
    }

    default Project.Builder append(Project.Builder builder) {
        return builder
                .documents(this.documents())
                .renders(this.renderPaths());
    }

    static ProjectTemplate fromPaths(String name, Class<? extends ProjectTemplate> relativeTo, Set<Path> templatePaths, Set<Path> renderPaths) throws IOException {
        Map<Path, InputStream> templates = templatePaths.stream().collect(Collectors.toMap(
                Function.identity(),
                path -> Resources.asInputStream(relativeTo, path.toString())
        ));
        return fromInputStreams(name, templates, renderPaths);
    }

    static ProjectTemplate fromInputStreams(String name, Map<Path, InputStream> templates, Set<Path> renderPaths) throws IOException {
        return fromReaders(
                name,
                Maps.mapValues(templates, InputStreamReader::new),
                renderPaths
        );
    }

    static ProjectTemplate fromReaders(String name, Map<Path, Reader> templates, Set<Path> renderPaths) throws IOException {
        return fromCharSequences(
                name,
                Maps.mapValues(templates, reader -> new ReaderCharSequence(reader, 4096)),
                renderPaths
        );
    }

    static ProjectTemplate fromCharSequences(String name, Map<Path, CharSequence> templates, Set<Path> renderPaths) throws IOException {
        Parser parser = ParserFactory.instance().create(Map.of());
        Map<Path, Document> documents = new LinkedHashMap<>();
        for (Map.Entry<Path, CharSequence> e : templates.entrySet()) {
            Document document = parser.parse(ParseContext.of(e.getKey(), e.getValue()));
            documents.put(e.getKey(), document);
        }
        return of(name, documents, renderPaths);
    }

    static ProjectTemplate of(String name, Map<Path, Document> documents, Set<Path> renderPaths) {
        return new Default(name, documents, renderPaths);
    }

    record Default(String name, Map<Path, Document> documents, Set<Path> renderPaths) implements ProjectTemplate {}

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
        public Map<Path, Document> documents() {
            return source.documents();
        }
        @Override
        public Set<Path> renderPaths() {
            return source.renderPaths();
        }
    }
}
