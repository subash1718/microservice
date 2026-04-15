package com.microservice.order_service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveOrder() {

        Order order = new Order();
	order.setProductName("Laptop");
        order.setQuantity(2);

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals("Laptop",savedOrder.getProductName());
        assertEquals(2, savedOrder.getQuantity());
    }
}
