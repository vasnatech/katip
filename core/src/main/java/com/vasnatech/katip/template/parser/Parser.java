package com.vasnatech.katip.template.parser;

import com.vasnatech.commons.text.ReaderCharSequence;
import com.vasnatech.katip.template.document.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

public interface Parser {

    default Document parse(InputStream in) throws IOException {
        return parse(new ReaderCharSequence(in, 4096));
    }

    default Document parse(Reader reader) throws IOException {
        return parse(new ReaderCharSequence(reader, 4096));
    }

    default Document parse(CharSequence content) throws IOException {
        return parse(ParseContext.of(Path.of("dummy"), content));
    }

    Document parse(ParseContext parseContext) throws IOException;
}
