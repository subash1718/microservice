package com.microservice.order_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int quantity;
    private double price;
    private String status;

    // ✅ Constructors
    public Order() {}

    public Order(String productName, int quantity, double price, String status) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}