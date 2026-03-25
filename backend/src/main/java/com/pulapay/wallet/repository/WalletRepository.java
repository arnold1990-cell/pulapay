package com.pulapay.wallet.repository;

import com.pulapay.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserPhoneNumber(String phoneNumber);
    Optional<Wallet> findByWalletNumber(String walletNumber);
}
