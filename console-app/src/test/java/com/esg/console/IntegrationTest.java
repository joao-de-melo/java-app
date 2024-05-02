package com.esg.console;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegrationTest {
    private final WireMockServer mockServer = new WireMockServer(0);

    @BeforeEach
    void setUp() {
        mockServer.start();
        mockServer.addStubMapping(WireMock.put(WireMock.anyUrl())
                .willReturn(WireMock.aResponse().withStatus(200))
                .build());
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
    }

    @Test
    void happyPath() {
        Main.main("src/test/resources/example.csv", "http://localhost:%d".formatted(mockServer.port()));

        assertThat(
                mockServer.findAll(RequestPatternBuilder.allRequests()).size(),
                is(10000)
        );
    }

    @Test
    void invalidArgs() {
        try {
            Main.main("one");
        } catch (Throwable e) {
            assertThat(e.getMessage(), is("Usage: app <csv file> <url>"));
        }
    }

    @Test
    void fileNotFound() {
        try {
            Main.main("src/test/resources/notFound.csv", "http://localhost:"+mockServer.port());
        } catch (Throwable e) {
            assertThat(e.getMessage(), is("File not found: src/test/resources/notFound.csv"));
        }
    }
}
