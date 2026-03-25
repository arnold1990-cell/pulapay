package com.pulapay.wallet.controller;

import com.pulapay.common.dto.ApiResponse;
import com.pulapay.user.entity.User;
import com.pulapay.wallet.dto.FundWalletRequest;
import com.pulapay.wallet.dto.WalletBalanceResponse;
import com.pulapay.wallet.dto.WalletResponse;
import com.pulapay.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) { this.walletService = walletService; }

    @GetMapping
    public ApiResponse<WalletResponse> wallet(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("Wallet details", walletService.getWallet(user));
    }

    @GetMapping("/balance")
    public ApiResponse<WalletBalanceResponse> balance(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("Wallet balance", walletService.getBalance(user));
    }

    @PostMapping("/fund")
    public ApiResponse<WalletResponse> fund(@AuthenticationPrincipal User user, @Valid @RequestBody FundWalletRequest request) {
        return ApiResponse.ok("Wallet funded", walletService.fundWallet(user, request.amount(), "Local funding endpoint"));
    }
}
