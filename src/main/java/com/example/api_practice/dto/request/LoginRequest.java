package com.example.api_practice.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequest {
    @NotBlank(message = "username must not be blank")
    @Schema(description = "ユーザー名", example = "test")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Schema(description = "パスワード", example = "password")
    private String password;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
