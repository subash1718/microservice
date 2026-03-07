package com.microservice.auth_service.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.microservice.auth_service.security.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,String> request){

        String username = request.get("username");
        String password = request.get("password");

        if("subhash".equals(username) && "1234".equals(password)){

            String token = JwtUtil.generateToken(username);

            return Map.of(
                "token", token,
                "success", true
            );
        }

        return Map.of(
            "success", false
        );
    }
}
