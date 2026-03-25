package com.pulapay.admin.controller;

import com.pulapay.admin.dto.AdminFundWalletRequest;
import com.pulapay.admin.dto.DashboardStatsResponse;
import com.pulapay.admin.service.AdminService;
import com.pulapay.common.dto.ApiResponse;
import com.pulapay.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) { this.adminService = adminService; }

    @PostMapping("/fund-wallet")
    public ApiResponse<Void> fund(@AuthenticationPrincipal User user, @Valid @RequestBody AdminFundWalletRequest request) {
        adminService.fundWallet(user, request);
        return ApiResponse.ok("Wallet funded by admin", null);
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStatsResponse> dashboard() {
        return ApiResponse.ok("Dashboard stats", adminService.dashboard());
    }
}
