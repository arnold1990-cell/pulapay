package com.pulapay.transaction.dto;

import com.pulapay.transaction.entity.TransactionStatus;
import com.pulapay.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(String reference, String senderWalletNumber, String receiverWalletNumber, BigDecimal amount,
                                  BigDecimal fee, BigDecimal netAmount, TransactionType type, TransactionStatus status,
                                  String narration, Instant createdAt) {}
