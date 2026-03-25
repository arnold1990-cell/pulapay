package com.pulapay.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 2, max = 120) @JsonAlias("fullName") String name,
        @NotBlank @Email String email,
        @JsonAlias("phoneNumber") String phoneNumber,
        @NotBlank @Size(min = 8, max = 64) String password
) {
}
