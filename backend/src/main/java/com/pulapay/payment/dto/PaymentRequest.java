package com.pulapay.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(@NotNull @DecimalMin("0.01") BigDecimal amount, @NotBlank String merchantName, @NotBlank String merchantReference) {}
