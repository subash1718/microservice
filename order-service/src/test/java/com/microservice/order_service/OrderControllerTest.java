package com.microservice.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ── GET /orders ────────────────────────────────────────────────

    @Test
    void shouldGetAllOrders() throws Exception {
        Order o1 = new Order("Laptop", 1, 1000, "CREATED");
        when(orderService.getAllOrders()).thenReturn(List.of(o1));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].productName", is("Laptop")));
    }

    @Test
    void shouldGetAllOrders_returnsEmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void shouldGetAllOrders_returnsMultipleOrders() throws Exception {
        Order o1 = new Order("Laptop", 1, 1000, "CREATED");
        Order o2 = new Order("Phone", 2, 500, "PAID");
        when(orderService.getAllOrders()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    // ── POST /orders ───────────────────────────────────────────────

    @Test
    void shouldCreateOrder() throws Exception {
        Order order = new Order("Book", 1, 50, "CREATED");
        when(orderService.createOrder(Mockito.any())).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("Book")));
    }

    @Test
    void shouldCreateOrder_andReturnCorrectStatus() throws Exception {
        Order order = new Order("Tablet", 1, 300, "CREATED");
        when(orderService.createOrder(Mockito.any())).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CREATED")));
    }

    // ── GET /orders/{id} ───────────────────────────────────────────

    @Test
    void shouldGetOrderById() throws Exception {
        Order order = new Order("TV", 1, 800, "CREATED");
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("TV")));
    }

    @Test
    void shouldGetOrderById_returnsCorrectFields() throws Exception {
        Order order = new Order("Camera", 2, 600, "PAID");
        when(orderService.getOrderById(5L)).thenReturn(order);

        mockMvc.perform(get("/orders/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("Camera")))
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    // ── POST /orders/payments/{id} ─────────────────────────────────

    @Test
    void shouldProcessPaymentSuccess() throws Exception {
        Order order = new Order("Item", 1, 100, "PAID");
        when(orderService.processPayment(2L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    @Test
    void shouldProcessPaymentFailure() throws Exception {
        Order order = new Order("Item", 1, 100, "FAILED");
        when(orderService.processPayment(1L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("FAILED")));
    }

    @Test
    void shouldProcessPayment_returnsOrderWithProductName() throws Exception {
        Order order = new Order("Watch", 1, 200, "PAID");
        when(orderService.processPayment(4L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("Watch")));
    }

    // ── GET /orders/{id}/status ────────────────────────────────────

    @Test
    void shouldGetOrderStatus_returnsPaidStatus() throws Exception {
        Order order = new Order("Mouse", 1, 20, "PAID");
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }

    @Test
    void shouldGetOrderStatus_returnsCreatedStatus() throws Exception {
        Order order = new Order("Desk", 1, 400, "CREATED");
        when(orderService.getOrderById(3L)).thenReturn(order);

        mockMvc.perform(get("/orders/3/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("CREATED"));
    }

    @Test
    void shouldGetOrderStatus_returnsFailedStatus() throws Exception {
        Order order = new Order("Chair", 1, 150, "FAILED");
        when(orderService.getOrderById(2L)).thenReturn(order);

        mockMvc.perform(get("/orders/2/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("FAILED"));
    }

    @Test
    void shouldReturnNotFoundStatus_whenOrderIsNull() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(null);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("NOT_FOUND"));
    }

    @Test
    void shouldReturnNotFoundStatus_whenOrderIsNullForDifferentId() throws Exception {
        when(orderService.getOrderById(99L)).thenReturn(null);

        mockMvc.perform(get("/orders/99/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("NOT_FOUND"));
    }
}