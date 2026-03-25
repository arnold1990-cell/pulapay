package com.pulapay.payment.dto;

import com.pulapay.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(String reference, BigDecimal amount, String merchantName, String merchantReference,
                              PaymentStatus status, Instant createdAt) {}
