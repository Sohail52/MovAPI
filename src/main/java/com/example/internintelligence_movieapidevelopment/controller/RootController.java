package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/test")
    public Map<String, String> test() {
        return Map.of("message", "Backend is working!");
    }

    @GetMapping("/api/db-health")
    public Map<String, Object> dbHealth() {
        try {
            long userCount = userRepository.count();
            return Map.of(
                "status", "OK",
                "message", "Database connection successful",
                "userCount", userCount
            );
        } catch (Exception e) {
            return Map.of(
                "status", "ERROR",
                "message", "Database connection failed: " + e.getMessage()
            );
        }
    }
}

