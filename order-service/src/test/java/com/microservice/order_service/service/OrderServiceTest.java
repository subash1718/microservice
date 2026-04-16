package com.microservice.order_service.service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order sample;

    @BeforeEach
    void setUp() {
        sample = new Order("Item", 1, 100, "CREATED");
    }

    @Test
    void createOrder_persistsViaRepository() {
        when(orderRepository.save(any(Order.class))).thenReturn(sample);

        Order result = orderService.createOrder(sample);

        assertEquals(sample, result);
        verify(orderRepository).save(sample);
    }

    @Test
    void save_persistsViaRepository() {
        when(orderRepository.save(any(Order.class))).thenReturn(sample);

        Order result = orderService.save(sample);

        assertEquals(sample, result);
        verify(orderRepository).save(sample);
    }

    @Test
    void getAllOrders_returnsRepositoryList() {
        when(orderRepository.findAll()).thenReturn(List.of(sample));

        List<Order> all = orderService.getAllOrders();

        assertEquals(1, all.size());
        assertEquals("Item", all.get(0).getProductName());
    }

    @Test
    void getOrderById_whenPresent_returnsOrder() {
        when(orderRepository.findById(5L)).thenReturn(Optional.of(sample));

        Order found = orderService.getOrderById(5L);

        assertEquals(sample, found);
    }

    @Test
    void getOrderById_whenMissing_returnsNull() {
        when(orderRepository.findById(7L)).thenReturn(Optional.empty());

        assertNull(orderService.getOrderById(7L));
    }

    @Test
    void getOrderStatusResponse_whenMissing_returnsNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals("NOT_FOUND", orderService.getOrderStatusResponse(1L));
    }

    @Test
    void getOrderStatusResponse_whenPresent_returnsStatus() {
        when(orderRepository.findById(2L)).thenReturn(Optional.of(sample));

        assertEquals("CREATED", orderService.getOrderStatusResponse(2L));
    }

    @Test
    void processPayment_evenId_setsPaidAndSaves() {
        when(orderRepository.findById(2L)).thenReturn(Optional.of(sample));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.processPayment(2L);

        assertEquals("PAID", result.getStatus());
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        assertEquals("PAID", captor.getValue().getStatus());
    }

    @Test
    void processPayment_oddId_setsFailedAndSaves() {
        when(orderRepository.findById(3L)).thenReturn(Optional.of(sample));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.processPayment(3L);

        assertEquals("FAILED", result.getStatus());
    }

    @Test
    void processPayment_whenOrderMissing_returnsNull() {
        when(orderRepository.findById(9L)).thenReturn(Optional.empty());

        assertNull(orderService.processPayment(9L));
    }
}
