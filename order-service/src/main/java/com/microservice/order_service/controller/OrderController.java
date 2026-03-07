package com.microservice.order_service.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public List<Map<String,Object>> getOrders(){

        return List.of(
            Map.of("id",1,"product","Laptop"),
            Map.of("id",2,"product","Phone")
        );
    }
}
