package com.pulapay.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(@NotBlank String recipientPhoneNumber, @NotNull @DecimalMin("0.01") BigDecimal amount, String narration) {}
