package com.pulapay.auth.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.common.exception.BadRequestException;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.entity.WalletStatus;
import com.pulapay.wallet.repository.WalletRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final WalletNumberGenerator walletNumberGenerator;
    private final AuditLogService auditLogService;

    public AuthService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService,
                       WalletNumberGenerator walletNumberGenerator, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.walletNumberGenerator = walletNumberGenerator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }

        String phoneNumber = resolvePhoneNumber(request.phoneNumber());

        User user = new User();
        user.setFullName(request.name());
        user.setPhoneNumber(phoneNumber);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setActive(true);
        user = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setWalletNumber(walletNumberGenerator.generate());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("BWP");
        wallet.setStatus(WalletStatus.ACTIVE);
        walletRepository.save(wallet);

        auditLogService.log("REGISTRATION", user.getEmail(), user.getRole(), String.valueOf(user.getId()), "User registered");
        return toAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        auditLogService.log("LOGIN", user.getEmail(), user.getRole(), String.valueOf(user.getId()), "User login successful");
        return toAuthResponse(user);
    }

    private String resolvePhoneNumber(String requestedPhoneNumber) {
        if (requestedPhoneNumber != null && !requestedPhoneNumber.isBlank()) {
            if (userRepository.existsByPhoneNumber(requestedPhoneNumber)) {
                throw new BadRequestException("Phone number already registered");
            }
            return requestedPhoneNumber;
        }

        String generatedPhoneNumber;
        do {
            generatedPhoneNumber = "SYS" + (System.currentTimeMillis() % 1_000_000_0000L)
                    + ThreadLocalRandom.current().nextInt(10, 99);
            generatedPhoneNumber = generatedPhoneNumber.substring(0, Math.min(generatedPhoneNumber.length(), 20));
        } while (userRepository.existsByPhoneNumber(generatedPhoneNumber));
        return generatedPhoneNumber;
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(
                jwtService.generateToken(user.getEmail()),
                new AuthResponse.UserInfo(user.getId(), user.getFullName(), user.getEmail(), user.getRole())
        );
    }
}
