package com.esg.console.process;

import com.esg.console.io.LineIterator;
import com.esg.console.io.LineIteratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.*;

public class Orchestrator implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Orchestrator.class);

    private final LineIteratorFactory lineGeneratorFactory;
    private final LineProcessorFactory lineProcessorFactory;
    private final ExecutorService executor;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public Orchestrator(LineIteratorFactory lineGeneratorFactory, LineProcessorFactory lineProcessorFactory, ExecutorService executor) {
        this.lineGeneratorFactory = lineGeneratorFactory;
        this.lineProcessorFactory = lineProcessorFactory;
        this.executor = executor;
    }

    public Orchestrator process(Reader reader) throws IOException {
        try (LineIterator lineIterator = lineGeneratorFactory.create(reader)) {
            if (lineIterator.hasNext()) lineIterator.next(); // Skip header
            while (lineIterator.hasNext()) {
                executor.submit(lineProcessorFactory.create(lineIterator.next()));
            }
        } finally {
            countDownLatch.countDown();
        }
        return this;
    }

    public void join () {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("Error waiting for processing to complete", e);
        }
    }

    @Override
    public void close() throws InterruptedException {
        countDownLatch.await();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }
}
