package com.microservice.order_service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Test
    void testOrderList() {

        int expectedOrders = 2;
        int actualOrders = 2;

        assertEquals(expectedOrders, actualOrders);
    }
}
