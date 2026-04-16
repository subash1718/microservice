package com.microservice.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        run(args);
    }

    /**
     * Separated so tests can start and close the context without leaving a running app.
     */
    static ConfigurableApplicationContext run(String... args) {
        return SpringApplication.run(OrderServiceApplication.class, args);
    }
}
