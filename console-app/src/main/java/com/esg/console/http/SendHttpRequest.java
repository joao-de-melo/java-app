package com.esg.console.http;

import com.esg.console.model.CustomerDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.functions.Consumer;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SendHttpRequest implements Consumer<CustomerDetails>, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(SendHttpRequest.class);
    private final String targetUrl;
    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper;

    public SendHttpRequest(String targetUrl, CloseableHttpClient client, ObjectMapper objectMapper) {
        this.targetUrl = targetUrl;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public void accept(CustomerDetails customerDetails) {
        try {
            HttpPut httpPost = new HttpPut(targetUrl);
            try {
                httpPost.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(customerDetails), ContentType.APPLICATION_JSON));
            } catch (JsonProcessingException e) {
                throw new IOException("Failed to serialize customer details", e);
            }

            client.execute(httpPost, new BasicHttpClientResponseHandler());
            logger.info("Customer details for {} sent successfully", customerDetails.customerRef());
        } catch (IOException e) {
            throw new RuntimeException("Could not send customer details", e);
        }
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}
