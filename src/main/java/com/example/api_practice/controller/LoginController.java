package com.example.api_practice.controller;

import com.example.api_practice.dto.request.LoginRequest;
import com.example.api_practice.dto.response.LoginResponse;
import com.example.api_practice.security.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "認証API")
@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "ユーザーログイン",
            description = "ユーザー名とパスワードとログインしJWTトークンを取得します"
    )
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUsername());

        return new LoginResponse(token);
    }
}
