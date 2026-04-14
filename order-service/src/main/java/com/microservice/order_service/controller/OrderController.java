package com.microservice.order_service.controller;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // ✅ Constructor Injection (BEST PRACTICE)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Get all orders
    @GetMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    // ✅ Create order (with inventory check)
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // ✅ Get order by ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // ✅ Payment API (US2 + US5)
    @PostMapping("/payments/{id}")
    public Order processPayment(@PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        boolean success = true; // simulate payment

        if (success) {
            order.setStatus("PAID");
        } else {
            order.setStatus("FAILED");
        }

        // ✅ Notification (for demo)
        System.out.println("Notification: Order " + id + " status = " + order.getStatus());

        return orderService.save(order);
    }

    // ✅ Get order status (US4)
    @GetMapping("/{id}/status")
    public String getOrderStatus(@PathVariable Long id) {
        return orderService.getOrderById(id).getStatus();
    }
}