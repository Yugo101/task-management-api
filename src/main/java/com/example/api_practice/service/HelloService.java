package com.example.api_practice.service;

import com.example.api_practice.dto.response.HelloResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class HelloService {
    public HelloResponse sayHello() {
        return new HelloResponse("hello", LocalDateTime.now());
    }
}
