package com.microservice.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order_service.controller.OrderController;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Test GET all orders
    @Test
    void testGetAllOrders() throws Exception {
        Order order = new Order("Laptop", 2, 1000.0, "CREATED");

        when(orderService.getAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"));
    }

    // ✅ Test CREATE order
    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order("Phone", 1, 500.0, "CREATED");

        when(orderService.createOrder(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Phone"));
    }

    // ✅ Test GET order by ID
    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order("Tablet", 1, 300.0, "CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Tablet"));
    }

    // ✅ Test PAYMENT
    @Test
    void testProcessPayment() throws Exception {
        Order order = new Order("Watch", 1, 200.0, "CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    // ✅ Test GET order status
    @Test
    void testGetOrderStatus() throws Exception {
        Order order = new Order("Shoes", 1, 100.0, "PAID");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }
}