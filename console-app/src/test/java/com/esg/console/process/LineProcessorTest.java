package com.esg.console.process;

import com.esg.console.csv.LineCSVParser;
import com.esg.console.http.SendHttpRequest;
import com.esg.console.model.CustomerDetails;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class LineProcessorTest {
    private final LineCSVParser csvParser = mock(LineCSVParser.class);
    private final SendHttpRequest httpRequest = mock(SendHttpRequest.class);
    private final LineProcessor underTest = new LineProcessor("line", csvParser, httpRequest);

    @Test
    void whenParserReturnsEmpty() {
        when(csvParser.apply("line")).thenReturn(Optional.empty());

        underTest.run();

        verifyNoInteractions(httpRequest);
    }

    @Test
    void whenParserReturnsNonEmpty() {
        CustomerDetails customerDetails = mock(CustomerDetails.class);
        when(csvParser.apply("line")).thenReturn(Optional.of(customerDetails));

        underTest.run();

        verify(httpRequest).accept(customerDetails);
    }
}