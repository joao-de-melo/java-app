package com.esg.console.io;

import java.io.Reader;

public class LineIteratorFactory {
    public LineIterator create(Reader file) {
        return new LineIterator(file);
    }
}
