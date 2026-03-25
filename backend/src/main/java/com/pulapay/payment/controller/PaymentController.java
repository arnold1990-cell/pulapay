package com.pulapay.payment.controller;

import com.pulapay.common.dto.ApiResponse;
import com.pulapay.payment.dto.PaymentRequest;
import com.pulapay.payment.dto.PaymentResponse;
import com.pulapay.payment.service.PaymentService;
import com.pulapay.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public ApiResponse<PaymentResponse> create(@AuthenticationPrincipal User user, @Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment successful", paymentService.create(user, request));
    }

    @GetMapping
    public ApiResponse<List<PaymentResponse>> list(@AuthenticationPrincipal User user) {
        return ApiResponse.ok("Payments", paymentService.list(user));
    }
}
