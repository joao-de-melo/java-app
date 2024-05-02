package com.esg.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String... args) {
        start(args);
    }

    public static ConfigurableApplicationContext start(String... args) {
        return SpringApplication.run(Main.class, args);
    }
}
