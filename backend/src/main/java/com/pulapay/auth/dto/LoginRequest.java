package com.pulapay.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email @JsonAlias("phoneNumber") String email,
        @NotBlank String password
) {}
