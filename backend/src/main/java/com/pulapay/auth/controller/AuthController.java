package com.pulapay.auth.controller;

import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.auth.service.AuthService;
import com.pulapay.common.dto.ApiResponse;
import com.pulapay.user.dto.UserResponse;
import com.pulapay.user.entity.User;
import com.pulapay.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/api/v1/auth"})
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok("Registration successful", authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("Login successful", authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("Authenticated user", userService.getMe(user));
    }
}
