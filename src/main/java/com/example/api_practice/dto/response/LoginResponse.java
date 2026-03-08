package com.example.api_practice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {
    @Schema(description = "JWTアクセストークン")
    private String token;

    public LoginResponse(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
