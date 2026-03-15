package com.example.api_practice.controller;

import com.example.api_practice.dto.response.HelloResponse;
import com.example.api_practice.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public HelloResponse hello() {
        return helloService.sayHello();
    }
}

