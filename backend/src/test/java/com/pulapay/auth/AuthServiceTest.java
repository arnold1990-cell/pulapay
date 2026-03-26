package com.pulapay.auth;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.common.exception.BadRequestException;
import com.pulapay.common.exception.UnauthorizedException;
import com.pulapay.auth.service.AuthService;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Test
    void registerReturnsTokenAndFrontendFriendlyUser() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(encoder.encode("password123")).thenReturn("hashed");
        when(walletNumberGenerator.generate()).thenReturn("2600000001");
        when(jwt.generateToken("jane@example.com")).thenReturn("token-123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthService service = new AuthService(userRepository, walletRepository, encoder, jwt, walletNumberGenerator, audit);

        AuthResponse response = service.register(new RegisterRequest("Jane Doe", "jane@example.com", null, "password123"));

        assertEquals("token-123", response.token());
        assertEquals("Jane Doe", response.user().name());
        assertEquals("jane@example.com", response.user().email());
        assertEquals(Role.USER, response.user().role());
    }

    @Test
    void loginAuthenticatesAndReturnsToken() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        User user = new User();
        user.setFullName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPhoneNumber("SYS123");
        user.setRole(Role.USER);

        user.setPassword("hashed-password");

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwt.generateToken("jane@example.com")).thenReturn("token-456");

        AuthService service = new AuthService(userRepository, walletRepository, encoder, jwt, walletNumberGenerator, audit);

        AuthResponse response = service.login(new LoginRequest("jane@example.com", "password123"));

        assertEquals("token-456", response.token());
        assertEquals("jane@example.com", response.user().email());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        AuthService service = new AuthService(userRepository, walletRepository, encoder, jwt, walletNumberGenerator, audit);

        assertThrows(BadRequestException.class, () ->
                service.register(new RegisterRequest("Jane Doe", "jane@example.com", null, "password123")));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginRejectsInvalidCredentials() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        AuthService service = new AuthService(userRepository, walletRepository, encoder, jwt, walletNumberGenerator, audit);

        User user = new User();
        user.setFullName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPhoneNumber("SYS123");
        user.setRole(Role.USER);
        user.setPassword("hashed-password");

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrongpassword", "hashed-password")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () ->
                service.login(new LoginRequest("jane@example.com", "wrongpassword")));
    }
}
