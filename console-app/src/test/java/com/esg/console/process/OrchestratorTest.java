package com.esg.console.process;

import com.esg.console.io.LineIterator;
import com.esg.console.io.LineIteratorFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class OrchestratorTest {
    private final LineIteratorFactory iteratorFactory = mock(LineIteratorFactory.class);
    private final LineProcessorFactory lineProcessorFactory = mock(LineProcessorFactory.class);
    private final ExecutorService executorService = mock(ExecutorService.class);
    private final Reader reader = mock(Reader.class);
    private final LineIterator lineIterator = mock(LineIterator.class);
    private final Orchestrator underTest = new Orchestrator(iteratorFactory, lineProcessorFactory, executorService);

    @BeforeEach
    void setUp() {
        when(iteratorFactory.create(reader)).thenReturn(lineIterator);
    }

    @Test
    void errorClosingIterator() throws Exception {
        IOException exception = new IOException("example");
        when(lineIterator.hasNext()).thenReturn(true, false);

        doThrow(exception).when(lineIterator).close();

        try {
            underTest.process(reader);
            fail();
        } catch (Throwable e) {
            assertThat(e, Matchers.is(exception));
        }
    }

    @Test
    void firstLineSkipped() throws Exception {
        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenReturn("line1");

        underTest.process(reader);

        verifyNoInteractions(lineProcessorFactory);
        verifyNoInteractions(executorService);
    }

    @Test
    void restOfLinesProcessed() throws Exception {
        LineProcessor lineProcessor1 = mock(LineProcessor.class);
        LineProcessor lineProcessor2 = mock(LineProcessor.class);
        when(lineIterator.hasNext()).thenReturn(true, true, true, false);
        when(lineIterator.next()).thenReturn("line1", "line2", "line3");
        when(lineProcessorFactory.create("line2")).thenReturn(lineProcessor1);
        when(lineProcessorFactory.create("line3")).thenReturn(lineProcessor2);

        underTest.process(reader);

        verify(executorService).submit(lineProcessor1);
        verify(executorService).submit(lineProcessor2);
    }
}