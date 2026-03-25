package com.pulapay.transaction.controller;

import com.pulapay.common.dto.ApiResponse;
import com.pulapay.transaction.dto.TransactionResponse;
import com.pulapay.transaction.service.TransactionService;
import com.pulapay.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/transactions", "/api/v1/transactions"})
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) { this.transactionService = transactionService; }

    @GetMapping
    public ApiResponse<List<TransactionResponse>> list(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("Transactions", transactionService.list(user));
    }

    @GetMapping("/{reference}")
    public ApiResponse<TransactionResponse> get(@AuthenticationPrincipal User user, @PathVariable String reference) {
        return ApiResponse.ok("Transaction", transactionService.getByReference(user, reference));
    }
}
