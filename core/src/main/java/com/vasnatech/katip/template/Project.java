package com.vasnatech.katip.template;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.katip.template.document.Document;
import com.vasnatech.katip.template.parser.Parser;
import com.vasnatech.katip.template.parser.ParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Project(Path root, Map<Path, Document> documents) {

    public Document document(String path) {
        return documents.get(Path.of(path));
    }

    public Document document(Path path) {
        return documents.get(path);
    }

    public static Project from(String root, String... paths) throws IOException {
        return from(Path.of(root), Stream.of(paths).map(Path::of).collect(Collectors.toSet()));
    }

    public static Project from(Path root, Path... paths) throws IOException {
        return from(root, Set.of(paths));
    }

    public static Project from(Path root, Collection<Path> relativePaths) throws IOException {
        Parser parser = ParserFactory.instance().create(Map.of());
        Map<Path, Document> documents = new LinkedHashMap<>(relativePaths.size());
        for (Path relativePath : relativePaths) {
            Path path = root.resolve(relativePath);
            try (Reader reader = new InputStreamReader(Resources.asInputStream(path.toString()))) {
                Document document = parser.parse(reader);
                documents.put(relativePath, document);
            }
        }
        return new Project(root, documents);
    }
}
