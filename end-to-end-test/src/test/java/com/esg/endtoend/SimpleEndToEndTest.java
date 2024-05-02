package com.esg.endtoend;

import com.esg.rest.persistence.CustomerDetailsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleEndToEndTest {
    @Test
    void integrateApps() {
        ConfigurableApplicationContext app = com.esg.rest.Main.start("--server.port=8080");

        com.esg.console.Main.main("src/test/resources/example.csv", "http://localhost:8080");

        assertThat(StreamSupport.stream(
                app.getBean(CustomerDetailsRepository.class).findAll().spliterator(),
                false
        ).count(), Matchers.is(10000L));
    }
}
