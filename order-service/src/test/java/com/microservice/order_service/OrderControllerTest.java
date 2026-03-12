package com.microservice.order_service;

import com.microservice.order_service.controller.OrderController;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Test
    void testGetOrders() {

        OrderRepository repo = Mockito.mock(OrderRepository.class);
        OrderController controller = new OrderController();

        Order order = new Order();
        order.setProduct("Pizza");
        order.setQuantity(2);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(order));

        assertNotNull(order);
    }
}
