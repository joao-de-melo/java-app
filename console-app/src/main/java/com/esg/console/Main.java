package com.esg.console;

import com.esg.console.csv.LineCSVParser;
import com.esg.console.http.SendHttpRequest;
import com.esg.console.io.LineIteratorFactory;
import com.esg.console.process.LineProcessorFactory;
import com.esg.console.process.Orchestrator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String... args) {
        if (args.length != 2) {
            throw new RuntimeException("Usage: app <csv file> <url>");
        }

        String csvFile = args[0];
        String url = args[1];

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + csvFile, e);
        }

        SendHttpRequest sendHttpRequest = new SendHttpRequest(url, HttpClients.createDefault(), new ObjectMapper());
        LineProcessorFactory lineProcessorFactory = new LineProcessorFactory(new LineCSVParser(), sendHttpRequest);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, 10, 100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        try (Orchestrator orchestrator = new Orchestrator(new LineIteratorFactory(), lineProcessorFactory, threadPoolExecutor)) {
            orchestrator.process(fileReader).join();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error processing file: " + csvFile, e);
        }
    }
}
