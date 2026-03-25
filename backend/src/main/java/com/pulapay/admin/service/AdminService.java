package com.pulapay.admin.service;

import com.pulapay.admin.dto.AdminFundWalletRequest;
import com.pulapay.admin.dto.DashboardStatsResponse;
import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.ResourceNotFoundException;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.repository.WalletRepository;
import com.pulapay.wallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AdminService {
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogService auditLogService;

    public AdminService(WalletRepository walletRepository, WalletService walletService, UserRepository userRepository,
                        TransactionRepository transactionRepository, AuditLogService auditLogService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public void fundWallet(User admin, AdminFundWalletRequest request) {
        Wallet wallet = walletRepository.findByWalletNumber(request.walletNumber()).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        walletService.fundWallet(wallet.getUser(), request.amount(), "Admin funding");
        auditLogService.log("ADMIN_FUNDING", admin.getPhoneNumber(), admin.getRole(), wallet.getWalletNumber(), "Admin funded wallet");
    }

    public DashboardStatsResponse dashboard() {
        BigDecimal totalVolume = transactionRepository.findAll().stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DashboardStatsResponse(userRepository.count(), walletRepository.count(), transactionRepository.count(), totalVolume);
    }
}
