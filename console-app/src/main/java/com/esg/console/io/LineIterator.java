package com.esg.console.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class LineIterator implements AutoCloseable, Iterator<String> {
    private final BufferedReader reader;
    private final Iterator<String> iterator;

    public LineIterator(Reader reader) {
        this.reader = new BufferedReader(reader);
        this.iterator = this.reader.lines().iterator();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String next() {
        return iterator.next();
    }
}
