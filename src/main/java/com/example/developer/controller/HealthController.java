package com.example.developer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/health")
    public String checkHealth() {
        try {
            redisTemplate.opsForValue().set("health", "OK");
            String value = (String) redisTemplate.opsForValue().get("health");
            return "Health Check OK. Redis Value: " + value;
        } catch (Exception e) {
            return "Health Check Failed: " + e.getMessage();
        }
    }
}