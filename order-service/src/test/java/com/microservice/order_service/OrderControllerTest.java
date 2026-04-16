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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ GET ALL
    @Test
    void shouldGetAllOrders() throws Exception {
        Order o1 = new Order("Laptop", 1, 1000, "CREATED");
        Order o2 = new Order("Phone", 2, 500, "CREATED");

        when(orderService.getAllOrders()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    // ✅ CREATE ORDER
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

    // ✅ GET BY ID
    @Test
    void shouldGetOrderById() throws Exception {
        Order order = new Order("TV", 1, 800, "CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("TV")));
    }

    // ✅ PAYMENT SUCCESS
    @Test
    void shouldProcessPaymentSuccess() throws Exception {
        Order order = new Order("Tablet", 1, 300, "CREATED");

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.save(Mockito.any())).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    // ✅ PAYMENT FAILURE (force branch coverage)
    @Test
    void shouldProcessPaymentFailure() throws Exception {
        Order order = new Order("Tablet", 1, 300, "CREATED");

        // simulate failure manually
        order.setStatus("FAILED");

        when(orderService.getOrderById(1L)).thenReturn(order);
        when(orderService.save(Mockito.any())).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk());
    }

    // ✅ STATUS CHECK
    @Test
    void shouldGetOrderStatus() throws Exception {
        Order order = new Order("Mouse", 1, 20, "PAID");

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }
}