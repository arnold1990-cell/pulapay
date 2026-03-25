package com.pulapay.transaction.service;

import com.pulapay.common.exception.ResourceNotFoundException;
import com.pulapay.transaction.dto.TransactionResponse;
import com.pulapay.transaction.entity.Transaction;
import com.pulapay.transaction.repository.TransactionRepository;
import com.pulapay.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) { this.repository = repository; }

    public List<TransactionResponse> list(User user) {
        return repository.findBySenderWalletUserPhoneNumberOrReceiverWalletUserPhoneNumberOrderByCreatedAtDesc(user.getPhoneNumber(), user.getPhoneNumber())
                .stream().map(this::toResponse).toList();
    }

    public TransactionResponse getByReference(User user, String ref) {
        Transaction t = repository.findByReference(ref).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (t.getSenderWallet() != null && t.getSenderWallet().getUser().getId().equals(user.getId())
                || t.getReceiverWallet() != null && t.getReceiverWallet().getUser().getId().equals(user.getId())) {
            return toResponse(t);
        }
        throw new ResourceNotFoundException("Transaction not found");
    }

    public TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getReference(),
                t.getSenderWallet() == null ? null : t.getSenderWallet().getWalletNumber(),
                t.getReceiverWallet() == null ? null : t.getReceiverWallet().getWalletNumber(),
                t.getAmount(), t.getFee(), t.getNetAmount(), t.getType(), t.getStatus(), t.getNarration(), t.getCreatedAt());
    }
}
