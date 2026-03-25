package com.pulapay.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Pattern(regexp = "^[0-9+]{8,15}$") String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 64) String password
) {}
