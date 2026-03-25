package com.pulapay.auth.dto;

import com.pulapay.user.entity.Role;

public record AuthResponse(String token, UserInfo user) {
    public record UserInfo(Long id, String name, String email, Role role) {}
}
