package com.esg.console.io;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

class LineIteratorTest {
    @Test
    void iterateThroughAllLines() {
        LineIterator underTest = new LineIterator(new StringReader("a\nb\nc"));

        List<String> actualList = new ArrayList<String>();
        while (underTest.hasNext()) {
            actualList.add(underTest.next());
        }

        assertThat(actualList, hasSize(3));
        assertThat(actualList, contains("a", "b", "c"));
    }
}