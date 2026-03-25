package com.pulapay.payment.entity;

import com.pulapay.wallet.entity.Wallet;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String reference;
    @ManyToOne(optional = false) @JoinColumn(name = "payer_wallet_id", nullable = false)
    private Wallet payerWallet;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private String merchantName;
    @Column(nullable = false)
    private String merchantReference;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PaymentStatus status;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @PrePersist void prePersist(){ createdAt = Instant.now(); }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public Wallet getPayerWallet() { return payerWallet; }
    public void setPayerWallet(Wallet payerWallet) { this.payerWallet = payerWallet; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getMerchantReference() { return merchantReference; }
    public void setMerchantReference(String merchantReference) { this.merchantReference = merchantReference; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
}
