package com.pulapay.auth.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonAlias;

public record LoginRequest(
        @NotBlank @JsonAlias("phoneNumber") String email,
        @NotBlank String password
) {}
