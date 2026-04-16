package com.microservice.order_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getOrders_returnsList() throws Exception {
        Order o1 = new Order("Laptop", 1, 1000, "CREATED");
        when(orderService.getAllOrders()).thenReturn(List.of(o1));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].productName", is("Laptop")));
    }

    @Test
    void getOrders_returnsEmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void createOrder_returnsBody() throws Exception {
        Order order = new Order("Book", 1, 50, "CREATED");
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("Book")))
                .andExpect(jsonPath("$.status", is("CREATED")));
    }

    @Test
    void getOrderById_returnsOrder() throws Exception {
        Order order = new Order("TV", 1, 800, "CREATED");
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("TV")));
    }

    @Test
    void processPayment_evenId_paid() throws Exception {
        Order order = new Order("Item", 1, 100, "PAID");
        when(orderService.processPayment(2L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    @Test
    void processPayment_oddId_failed() throws Exception {
        Order order = new Order("Item", 1, 100, "FAILED");
        when(orderService.processPayment(1L)).thenReturn(order);

        mockMvc.perform(post("/orders/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("FAILED")));
    }

    @Test
    void getOrderStatus_returnsStatusFromService() throws Exception {
        when(orderService.getOrderStatusResponse(1L)).thenReturn("PAID");

        mockMvc.perform(get("/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("PAID"));
    }

    @Test
    void getOrderStatus_returnsNotFoundWhenServiceSaysSo() throws Exception {
        when(orderService.getOrderStatusResponse(99L)).thenReturn("NOT_FOUND");

        mockMvc.perform(get("/orders/99/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("NOT_FOUND"));
    }
}
