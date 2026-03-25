package com.pulapay.user.dto;

import com.pulapay.user.entity.Role;

import java.time.Instant;

public record UserResponse(Long id, String fullName, String phoneNumber, String email, Role role, boolean active,
                           Instant createdAt, Instant updatedAt) {
}
