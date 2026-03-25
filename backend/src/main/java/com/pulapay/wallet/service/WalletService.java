package com.pulapay.wallet.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.ResourceNotFoundException;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.entity.Transaction;
import com.pulapay.transaction.entity.TransactionStatus;
import com.pulapay.transaction.entity.TransactionType;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.user.entity.User;
import com.pulapay.wallet.dto.WalletBalanceResponse;
import com.pulapay.wallet.dto.WalletResponse;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ReferenceGenerator referenceGenerator;
    private final AuditLogService auditLogService;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository,
                         ReferenceGenerator referenceGenerator, AuditLogService auditLogService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.referenceGenerator = referenceGenerator;
        this.auditLogService = auditLogService;
    }

    public WalletResponse getWallet(User user) { return toResponse(getByUser(user)); }

    public WalletBalanceResponse getBalance(User user) {
        Wallet wallet = getByUser(user);
        return new WalletBalanceResponse(wallet.getWalletNumber(), wallet.getBalance(), wallet.getCurrency());
    }

    @Transactional
    public WalletResponse fundWallet(User user, BigDecimal amount, String action) {
        Wallet wallet = getByUser(user);
        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet saved = walletRepository.save(wallet);

        Transaction txn = new Transaction();
        txn.setReference(referenceGenerator.generate("FUND"));
        txn.setReceiverWallet(saved);
        txn.setAmount(amount);
        txn.setFee(BigDecimal.ZERO);
        txn.setNetAmount(amount);
        txn.setType(TransactionType.FUNDING);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setNarration(action);
        transactionRepository.save(txn);

        auditLogService.log("WALLET_FUNDING", user.getPhoneNumber(), user.getRole(), txn.getReference(), action);
        return toResponse(saved);
    }

    public Wallet getByUser(User user) {
        return walletRepository.findByUserPhoneNumber(user.getPhoneNumber()).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    public Wallet getByWalletNumber(String walletNumber) {
        return walletRepository.findByWalletNumber(walletNumber).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    public WalletResponse toResponse(Wallet w) { return new WalletResponse(w.getWalletNumber(), w.getBalance(), w.getCurrency(), w.getStatus()); }
}
