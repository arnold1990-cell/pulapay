package com.pulapay.transfer.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.BadRequestException;
import com.pulapay.common.exception.InsufficientFundsException;
import com.pulapay.common.util.ReferenceGenerator;
import com.pulapay.transaction.entity.Transaction;
import com.pulapay.transaction.entity.TransactionStatus;
import com.pulapay.transaction.entity.TransactionType;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.transfer.dto.TransferRequest;
import com.pulapay.transfer.dto.TransferResponse;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.entity.WalletStatus;
import com.pulapay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ReferenceGenerator referenceGenerator;
    private final AuditLogService auditLogService;

    public TransferService(WalletRepository walletRepository, UserRepository userRepository, TransactionRepository transactionRepository,
                           ReferenceGenerator referenceGenerator, AuditLogService auditLogService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.referenceGenerator = referenceGenerator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public TransferResponse transfer(User sender, TransferRequest request) {
        if (sender.getPhoneNumber().equals(request.recipientPhoneNumber())) throw new BadRequestException("Sender cannot transfer to self");
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) throw new BadRequestException("Amount must be positive");

        Wallet senderWallet = walletRepository.findByUserPhoneNumber(sender.getPhoneNumber()).orElseThrow(() -> new BadRequestException("Sender wallet not found"));
        if (senderWallet.getStatus() != WalletStatus.ACTIVE) throw new BadRequestException("Sender wallet is not active");

        var recipient = userRepository.findByPhoneNumber(request.recipientPhoneNumber()).orElseThrow(() -> new BadRequestException("Recipient not found"));
        Wallet recipientWallet = walletRepository.findByUserPhoneNumber(recipient.getPhoneNumber()).orElseThrow(() -> new BadRequestException("Recipient wallet not found"));
        if (recipientWallet.getStatus() != WalletStatus.ACTIVE) throw new BadRequestException("Recipient wallet is not active");

        BigDecimal fee = request.amount().multiply(new BigDecimal("0.01"));
        BigDecimal totalDebit = request.amount().add(fee);
        if (senderWallet.getBalance().compareTo(totalDebit) < 0) throw new InsufficientFundsException("Insufficient funds");

        senderWallet.setBalance(senderWallet.getBalance().subtract(totalDebit));
        recipientWallet.setBalance(recipientWallet.getBalance().add(request.amount()));
        walletRepository.save(senderWallet);
        walletRepository.save(recipientWallet);

        Transaction txn = new Transaction();
        txn.setReference(referenceGenerator.generate("TRF"));
        txn.setSenderWallet(senderWallet);
        txn.setReceiverWallet(recipientWallet);
        txn.setAmount(request.amount());
        txn.setFee(fee);
        txn.setNetAmount(request.amount());
        txn.setType(TransactionType.TRANSFER);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setNarration(request.narration());
        txn = transactionRepository.save(txn);

        auditLogService.log("TRANSFER", sender.getPhoneNumber(), sender.getRole(), txn.getReference(), "Transfer to " + recipient.getPhoneNumber());
        return new TransferResponse(txn.getReference(), senderWallet.getWalletNumber(), recipientWallet.getWalletNumber(), txn.getAmount(),
                txn.getFee(), txn.getStatus(), "Transfer successful", txn.getCreatedAt());
    }
}
