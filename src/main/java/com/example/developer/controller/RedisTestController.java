package com.example.developer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis-test")
    public String testRedis() {
        try {
            redisTemplate.opsForValue().set("test", "Hello, Redis!");
            String value = (String) redisTemplate.opsForValue().get("test");
            return "Redis Test Successful. Value: " + value;
        } catch (Exception e) {
            return "Redis Test Failed: " + e.getMessage();
        }
    }
}