package com.pulapay.user.controller;

import com.pulapay.common.dto.ApiResponse;
import com.pulapay.user.dto.UpdateProfileRequest;
import com.pulapay.user.dto.UserResponse;
import com.pulapay.user.entity.User;
import com.pulapay.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("User profile", userService.getMe(user));
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> update(@AuthenticationPrincipal User user, @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.ok("Profile updated", userService.updateProfile(user, request));
    }
}
