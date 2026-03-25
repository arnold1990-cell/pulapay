package com.pulapay.payment.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.InsufficientFundsException;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.payment.dto.PaymentRequest;
import com.pulapay.payment.dto.PaymentResponse;
import com.pulapay.payment.entity.Payment;
import com.pulapay.payment.entity.PaymentStatus;
import com.pulapay.payment.repository.PaymentRepository;
import com.pulapay.transaction.entity.Transaction;
import com.pulapay.transaction.entity.TransactionStatus;
import com.pulapay.transaction.entity.TransactionType;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.user.entity.User;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ReferenceGenerator referenceGenerator;
    private final AuditLogService auditLogService;

    public PaymentService(PaymentRepository paymentRepository, WalletRepository walletRepository,
                          TransactionRepository transactionRepository, ReferenceGenerator referenceGenerator,
                          AuditLogService auditLogService) {
        this.paymentRepository = paymentRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.referenceGenerator = referenceGenerator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public PaymentResponse create(User user, PaymentRequest request) {
        Wallet wallet = walletRepository.findByUserPhoneNumber(user.getPhoneNumber()).orElseThrow();
        if (wallet.getBalance().compareTo(request.amount()) < 0) throw new InsufficientFundsException("Insufficient funds for payment");
        wallet.setBalance(wallet.getBalance().subtract(request.amount()));
        walletRepository.save(wallet);

        Payment p = new Payment();
        p.setReference(referenceGenerator.generate("PAY"));
        p.setPayerWallet(wallet);
        p.setAmount(request.amount());
        p.setMerchantName(request.merchantName());
        p.setMerchantReference(request.merchantReference());
        p.setStatus(PaymentStatus.SUCCESS);
        p = paymentRepository.save(p);

        Transaction t = new Transaction();
        t.setReference(referenceGenerator.generate("TXN"));
        t.setSenderWallet(wallet);
        t.setAmount(request.amount());
        t.setFee(BigDecimal.ZERO);
        t.setNetAmount(request.amount());
        t.setType(TransactionType.PAYMENT);
        t.setStatus(TransactionStatus.SUCCESS);
        t.setNarration("Payment to " + request.merchantName());
        transactionRepository.save(t);

        auditLogService.log("PAYMENT", user.getPhoneNumber(), user.getRole(), p.getReference(), "Merchant payment");
        return toResponse(p);
    }

    public List<PaymentResponse> list(User user) {
        return paymentRepository.findByPayerWalletUserPhoneNumberOrderByCreatedAtDesc(user.getPhoneNumber()).stream().map(this::toResponse).toList();
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(p.getReference(), p.getAmount(), p.getMerchantName(), p.getMerchantReference(), p.getStatus(), p.getCreatedAt());
    }
}
