package com.esg.rest;

import com.esg.rest.model.CustomerDetails;
import com.esg.rest.persistence.CustomerDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IntegrationTest {
    private final ConfigurableApplicationContext app = Main.start();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new JsonMapper();

    @AfterEach
    void tearDown() throws Exception {
        httpClient.close();
        app.close();
    }

    @Test
    void happyPath() throws Exception {
        HttpPut request = new HttpPut("http://localhost:%d".formatted(port()));

        request.setEntity(new ByteArrayEntity(
                objectMapper.writeValueAsBytes(new CustomerDetails(
                        "REF9989",
                        "Name9989",
                        "Street9989",
                        "Apt 9989",
                        "Town9989",
                        "County9989",
                        "CountryX",
                        "POST9989"
                )),
                ContentType.APPLICATION_JSON
        ));

        Boolean result = httpClient.execute(request, booleanResponse());

        assertThat(result, is(true));
        assertThat(app.getBean(CustomerDetailsRepository.class)
                .findById("REF9989").isPresent(), is(true));
    }

    private static AbstractHttpClientResponseHandler<Boolean> booleanResponse() {
        return new AbstractHttpClientResponseHandler<Boolean>() {
            @Override
            public Boolean handleEntity(HttpEntity entity) throws IOException {
                return Boolean.parseBoolean(new String(entity.getContent().readAllBytes()));
            }
        };
    }

    private int port () {
        return ((WebServerApplicationContext) app).getWebServer().getPort();
    }
}