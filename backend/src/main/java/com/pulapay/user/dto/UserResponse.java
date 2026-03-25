package com.pulapay.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pulapay.user.entity.Role;

import java.time.Instant;

public record UserResponse(Long id, String name, String phoneNumber, String email, Role role, boolean active,
                           Instant createdAt, Instant updatedAt) {
    @JsonProperty("fullName")
    public String fullName() {
        return name;
    }
}
