package com.microservice.order_service.controller;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/payments/{id}")
    public Order processPayment(@PathVariable Long id) {
        return orderService.processPayment(id);
    }

    @GetMapping("/{id}/status")
    public String getOrderStatus(@PathVariable Long id) {
        return orderService.getOrderStatusResponse(id);
    }
}
