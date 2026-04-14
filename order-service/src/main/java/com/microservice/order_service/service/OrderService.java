package com.microservice.order_service.service;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order createOrder(Order order) {
        if (order.getQuantity() > 5) {
            throw new RuntimeException("Not enough stock");
        }
        order.setStatus("CREATED");
        return repository.save(order);
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order save(Order order) {
        return repository.save(order);
    }
}
