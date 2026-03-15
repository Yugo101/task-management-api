package com.example.api_practice.dto.response;

import java.time.LocalDateTime;

public class HelloResponse {

    private String message;
    private LocalDateTime timestamp;

    public HelloResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage(){
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
