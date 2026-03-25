package com.pulapay.transfer.controller;

import com.pulapay.common.dto.ApiResponse;
import com.pulapay.transfer.dto.TransferRequest;
import com.pulapay.transfer.dto.TransferResponse;
import com.pulapay.transfer.service.TransferService;
import com.pulapay.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/transfers", "/api/v1/transfers"})
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) { this.transferService = transferService; }

    @PostMapping
    public ApiResponse<TransferResponse> transfer(@AuthenticationPrincipal User user, @Valid @RequestBody TransferRequest request) {
        return ApiResponse.ok("Transfer completed", transferService.transfer(user, request));
    }
}
