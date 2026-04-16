package com.microservice.order_service;

import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
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

    /**
     * Covers the same bootstrap path as {@code main} (delegates to {@link OrderServiceApplication#run(String...)}).
     * Calling {@code main} in-process would leave a second context running without a clean shutdown hook.
     */
    @Test
    void runStartsAndStopsStandaloneContext() {
        try (ConfigurableApplicationContext ctx = OrderServiceApplication.run(
                "--spring.main.web-application-type=none",
                "--spring.profiles.active=test")) {
            assertThat(ctx.getBean(OrderService.class)).isNotNull();
        }
    }
}
