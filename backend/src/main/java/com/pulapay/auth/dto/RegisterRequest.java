package com.pulapay.auth.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonAlias;

public record RegisterRequest(
        @NotBlank @JsonAlias("fullName") String name,
        @NotBlank @Email String email,
        @JsonAlias("phoneNumber") String phoneNumber,
        @NotBlank @Size(min = 8, max = 64) String password
) {}
