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

    @Test
    void shouldGetAllOrders() throws Exception {
        Order o1 = new Order("Laptop", 1, 1000, "CREATED");

        when(orderService.getAllOrders()).thenReturn(List.of(o1));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

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
    void shouldGetOrderById() throws Exception {
        Order order = new Order("TV", 1, 800, "CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
    }

    // ✅ PAYMENT SUCCESS
    @Test
    void shouldProcessPaymentSuccess() throws Exception {
        Order order = new Order("Item", 1, 100, "PAID");

        when(orderService.processPayment(2L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    // ✅ PAYMENT FAILURE
    @Test
    void shouldProcessPaymentFailure() throws Exception {
        Order order = new Order("Item", 1, 100, "FAILED");

        when(orderService.processPayment(1L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    // ✅ STATUS
    @Test
    void shouldGetOrderStatus() throws Exception {
        Order order = new Order("Mouse", 1, 20, "PAID");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }

    // ✅ NULL CASE (BOOST COVERAGE)
    @Test
    void shouldReturnNotFoundStatus() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(null);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("NOT_FOUND"));
    }
}