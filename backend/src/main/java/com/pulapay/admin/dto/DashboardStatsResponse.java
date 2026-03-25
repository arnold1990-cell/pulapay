package com.pulapay.admin.dto;

import java.math.BigDecimal;

public record DashboardStatsResponse(long totalUsers, long totalWallets, long totalTransactions, BigDecimal totalVolume) {}
