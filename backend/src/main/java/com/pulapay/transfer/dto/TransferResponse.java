package com.pulapay.transfer.dto;

import com.pulapay.transaction.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(String reference, String senderWalletNumber, String recipientWalletNumber, BigDecimal amount,
                               BigDecimal fee, TransactionStatus status, String message, Instant createdAt) {}
