package com.microservice.order_service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessPaymentSuccess() {
        Order order = new Order("Item", 1, 100, "CREATED");

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.processPayment(2L);

        assertEquals("PAID", result.getStatus());
    }

    @Test
    void shouldProcessPaymentFailure() {
        Order order = new Order("Item", 1, 100, "CREATED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.processPayment(1L);

        assertEquals("FAILED", result.getStatus());
    }

    @Test
    void shouldReturnNullIfOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order result = orderService.processPayment(1L);

        assertNull(result);
    }
}