package com.pulapay.auth.dto;

import com.pulapay.user.entity.Role;

public record AuthResponse(String token, UserInfo user) {
    public record UserInfo(String name, String email, String phoneNumber, Role role) {}
}
