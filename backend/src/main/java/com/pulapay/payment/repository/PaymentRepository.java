package com.pulapay.payment.repository;

import com.pulapay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPayerWalletUserPhoneNumberOrderByCreatedAtDesc(String phone);
}
