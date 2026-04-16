package com.microservice.order_service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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

    // ✅ CREATE ORDER
    @Test
    void shouldCreateOrder() {
        Order order = new Order("Laptop", 1, 1000, "CREATED");

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals("Laptop", result.getProductName());
        verify(orderRepository, times(1)).save(order);
    }

    // ✅ SAVE ORDER
    @Test
    void shouldSaveOrder() {
        Order order = new Order("Phone", 2, 500, "CREATED");

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertNotNull(result);
        assertEquals("Phone", result.getProductName());
    }

    // ✅ GET ALL
    @Test
    void shouldGetAllOrders() {
        Order o1 = new Order("Item1", 1, 100, "CREATED");
        Order o2 = new Order("Item2", 2, 200, "CREATED");

        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
    }

    // ✅ GET BY ID (FOUND)
    @Test
    void shouldReturnOrderWhenFound() {
        Order order = new Order("TV", 1, 800, "CREATED");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("TV", result.getProductName());
    }

    // ✅ GET BY ID (NOT FOUND) ⭐ VERY IMPORTANT
    @Test
    void shouldReturnNullWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order result = orderService.getOrderById(1L);

        assertNull(result);
    }
}