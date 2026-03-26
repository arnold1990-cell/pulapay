package com.pulapay.auth.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.common.exception.BadRequestException;
import com.pulapay.common.exception.UnauthorizedException;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.entity.Wallet;
import com.pulapay.wallet.entity.WalletStatus;
import com.pulapay.wallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WalletNumberGenerator walletNumberGenerator;
    private final AuditLogService auditLogService;

    public AuthService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, WalletNumberGenerator walletNumberGenerator,
                       AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.walletNumberGenerator = walletNumberGenerator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        log.debug("Register attempt for email={}", normalizedEmail);
        if (userRepository.existsByEmail(normalizedEmail)) {
            log.debug("Registration rejected: duplicate email={}", normalizedEmail);
            throw new BadRequestException("Email already registered");
        }

        String phoneNumber = resolvePhoneNumber(request.phoneNumber());

        User user = new User();
        user.setFullName(request.name());
        user.setPhoneNumber(phoneNumber);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setActive(true);
        user = userRepository.save(user);
        log.debug("Registration successful for email={}, userId={}", user.getEmail(), user.getId());

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setWalletNumber(walletNumberGenerator.generate());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("BWP");
        wallet.setStatus(WalletStatus.ACTIVE);
        walletRepository.save(wallet);

        auditLogService.log("REGISTRATION", user.getEmail(), user.getRole(), String.valueOf(user.getId()), "User registered");
        AuthResponse response = toAuthResponse(user);
        log.debug("Register response for email={}, tokenPresent={}, role={}", normalizedEmail,
                !response.token().isBlank(), response.user().role());
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        log.debug("Login attempt received for email={}", normalizedEmail);

        User user = userRepository.findByEmail(normalizedEmail).orElse(null);
        boolean userFound = user != null;
        log.debug("Login lookup result for email={}: userFound={}", normalizedEmail, userFound);

        if (!userFound) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new UnauthorizedException("Invalid email or password");
        }

        boolean passwordMatched = passwordEncoder.matches(request.password(), user.getPassword());
        log.debug("Password comparison for email={}: matches={}", normalizedEmail, passwordMatched);

        if (!passwordMatched) {
            throw new UnauthorizedException("Invalid email or password");
        }

        log.debug("Login successful for email={}, userId={}", user.getEmail(), user.getId());
        auditLogService.log("LOGIN", user.getEmail(), user.getRole(), String.valueOf(user.getId()), "User login successful");
        AuthResponse response = toAuthResponse(user);
        log.debug("Login response for email={}, tokenPresent={}, role={}", normalizedEmail,
                !response.token().isBlank(), response.user().role());
        return response;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
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
