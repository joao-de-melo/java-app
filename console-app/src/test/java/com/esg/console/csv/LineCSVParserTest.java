package com.esg.console.csv;

import com.esg.console.model.CustomerDetails;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LineCSVParserTest {
    private final LineCSVParser underTest = new LineCSVParser();


    @Test
    void emptyLine() {
        Optional<CustomerDetails> result = underTest.apply("");

        assertFalse(result.isPresent());
    }

    @Test
    void invalidLine() {
        Optional<CustomerDetails> result = underTest.apply("A,");

        assertFalse(result.isPresent());
    }


    @Test
    void validLine() {
        Optional<CustomerDetails> result = underTest.apply("1,John Doe,123 Main St,,Springfield,IL,USA,12345");

        assertTrue(result.isPresent());

        CustomerDetails customerDetails = result.get();
        assertEquals("1", customerDetails.customerRef());
        assertEquals("John Doe", customerDetails.customerName());
        assertEquals("123 Main St", customerDetails.addressLine1());
        assertEquals("", customerDetails.addressLine2());
        assertEquals("Springfield", customerDetails.town());
        assertEquals("IL", customerDetails.county());
        assertEquals("USA", customerDetails.country());
        assertEquals("12345", customerDetails.postcode());
    }
}