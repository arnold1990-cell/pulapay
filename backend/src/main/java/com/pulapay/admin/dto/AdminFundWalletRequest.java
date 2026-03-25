package com.pulapay.admin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AdminFundWalletRequest(@NotBlank String walletNumber, @NotNull @DecimalMin("0.01") BigDecimal amount) {}
