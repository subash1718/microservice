package com.microservice.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order_service.controller.OrderController;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ GET /orders
    @Test
    void testGetOrders() throws Exception {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }

    // ✅ POST /orders
    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

  
    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

  
    @Test
    void testProcessPayment() throws Exception {
        Order order = new Order();
        order.setStatus("CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.save(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    // ✅ GET /orders/{id}/status
    @Test
    void testGetOrderStatus() throws Exception {
        Order order = new Order();
        order.setStatus("PAID");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }
}