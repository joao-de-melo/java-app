package com.esg.console.process;

import com.esg.console.csv.LineCSVParser;
import com.esg.console.http.SendHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LineProcessor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(LineProcessor.class);

    private final String line;
    private final LineCSVParser parser;
    private final SendHttpRequest httpRequest;

    public LineProcessor(String line, LineCSVParser parser, SendHttpRequest httpRequest) {
        this.line = line;
        this.parser = parser;
        this.httpRequest = httpRequest;
    }

    @Override
    public void run() {
        try {
            parser.apply(line).ifPresent(httpRequest::accept);
        } catch (RuntimeException e) {
            logger.error("Line {} not sent", line);
            throw e;
        }
    }
}
