package com.pulapay.wallet.dto;

import com.pulapay.wallet.entity.WalletStatus;

import java.math.BigDecimal;

public record WalletResponse(String walletNumber, BigDecimal balance, String currency, WalletStatus status) {}
