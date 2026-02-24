package com.microservice.auth_service.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean validateUser(String username, String password) {

        // Temporary hardcoded validation (will replace with DB later)

        if ("subhash".equals(username) && "1234".equals(password)) {
            return true;
        }

        return false;
    }
}
