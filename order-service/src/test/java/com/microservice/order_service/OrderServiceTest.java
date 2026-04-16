package com.microservice.order_service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    // ✅ Test createOrder
    @Test
    void testOrderCreation() {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order saved = orderService.createOrder(order);

        assertNotNull(saved);
        assertEquals("CREATED", saved.getStatus());
    }

    // ✅ Test getAllOrders
    @Test
    void testGetAllOrders() {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order)); 

        assertEquals(1, orderService.getAllOrders().size());
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("CREATED", result.getStatus());
    }

    // ✅ Test save
    @Test
    void testSaveOrder() {
        Order order = new Order();
        order.setStatus("PAID");

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order saved = orderService.save(order);

        assertEquals("PAID", saved.getStatus());
    }
}