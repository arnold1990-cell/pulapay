package com.pulapay.auth.dto;

import com.pulapay.user.entity.Role;

public record AuthResponse(String accessToken, String phoneNumber, String fullName, Role role) {}
