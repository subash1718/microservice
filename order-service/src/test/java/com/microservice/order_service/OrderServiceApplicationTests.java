package com.microservice.order_service;

import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertThat(orderService).isNotNull();
    }
}
