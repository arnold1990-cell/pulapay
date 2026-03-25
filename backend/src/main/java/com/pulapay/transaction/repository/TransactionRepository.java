package com.pulapay.transaction.repository;

import com.pulapay.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderWalletUserPhoneNumberOrReceiverWalletUserPhoneNumberOrderByCreatedAtDesc(String sender, String receiver);
    Optional<Transaction> findByReference(String reference);
}
