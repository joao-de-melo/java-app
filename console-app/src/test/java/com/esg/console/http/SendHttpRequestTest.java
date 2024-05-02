package com.esg.console.http;

import com.esg.console.model.CustomerDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class SendHttpRequestTest {
    private final WireMockServer wireMockServer = new WireMockServer(0);
    private SendHttpRequest underTest;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        underTest = new SendHttpRequest(
                "http://localhost:%d".formatted(wireMockServer.port()),
                HttpClients.createDefault(),
                new ObjectMapper()
        );
    }

    @Test
    void invalidResponseShouldNotBreak() {
        wireMockServer.addStubMapping(
                WireMock.put(WireMock.anyUrl())
                        .willReturn(WireMock.aResponse().withStatus(500))
                        .build()
        );
        CustomerDetails customer = new CustomerDetails("ref123", "John Doe",
                "123 Main St", "Apt 4B", "Anytown",
                "Anycounty", "Anycountry", "12345");

        try {
            underTest.accept(customer);
        } catch (Throwable e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    @Test
    void happyPath() throws Throwable {
        wireMockServer.addStubMapping(
                WireMock.put(WireMock.anyUrl())
                        .willReturn(WireMock.aResponse().withStatus(200))
                        .build()
        );
        CustomerDetails customer = new CustomerDetails("ref123", "John Doe",
                "123 Main St", "Apt 4B", "Anytown",
                "Anycounty", "Anycountry", "12345");

        underTest.accept(customer);

        String requestBody = wireMockServer.findAll(RequestPatternBuilder.allRequests()).get(0).getBodyAsString();
        JSONAssert.assertEquals(
                new JSONObject()
                        .put("customerRef", "ref123")
                        .put("customerName", "John Doe")
                        .put("addressLine1", "123 Main St")
                        .put("addressLine2", "Apt 4B")
                        .put("town", "Anytown")
                        .put("county", "Anycounty")
                        .put("country", "Anycountry")
                        .put("postcode", "12345")
                        .toString(), requestBody, JSONCompareMode.STRICT);
    }
}