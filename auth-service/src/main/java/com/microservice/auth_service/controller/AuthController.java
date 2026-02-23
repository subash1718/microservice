package com.microservice.auth_service.controller;

import com.microservice.auth_service.dto.LoginRequest;
import com.microservice.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        boolean isValid = authService.validateUser(
                request.getUsername(),
                request.getPassword()
        );

        if (isValid) {
            return "Login successful";
        } else {
            return "Invalid credentials";
        }
    }
}
