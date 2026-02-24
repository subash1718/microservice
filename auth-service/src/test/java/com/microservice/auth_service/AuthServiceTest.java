package com.microservice.auth_service;

import com.microservice.auth_service.service.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void testValidUser() {
        assertTrue(authService.validateUser("subhash", "1234"));
    }

    @Test
    void testInvalidUser() {
        assertFalse(authService.validateUser("wrong", "wrong"));
    }
}
