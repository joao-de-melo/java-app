package com.esg.console.process;

import com.esg.console.csv.LineCSVParser;
import com.esg.console.http.SendHttpRequest;

public class LineProcessorFactory {
    private final LineCSVParser parser;
    private final SendHttpRequest httpRequest;

    public LineProcessorFactory(LineCSVParser parser, SendHttpRequest httpRequest) {
        this.parser = parser;
        this.httpRequest = httpRequest;
    }

    public LineProcessor create(String line) {
        return new LineProcessor(line, parser, httpRequest);
    }
}
