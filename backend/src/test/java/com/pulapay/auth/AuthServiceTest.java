package com.pulapay.auth;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.auth.service.AuthService;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Test
    void registerReturnsTokenAndFrontendFriendlyUser() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(encoder.encode("password123")).thenReturn("hashed");
        when(walletNumberGenerator.generate()).thenReturn("2600000001");
        when(jwt.generateToken("jane@example.com")).thenReturn("token-123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthService service = new AuthService(userRepository, walletRepository, encoder, manager, jwt, walletNumberGenerator, audit);

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
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);

        User user = new User();
        user.setFullName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPhoneNumber("SYS123");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(jwt.generateToken("jane@example.com")).thenReturn("token-456");

        AuthService service = new AuthService(userRepository, walletRepository, encoder, manager, jwt, walletNumberGenerator, audit);

        AuthResponse response = service.login(new LoginRequest("jane@example.com", "password123"));

        verify(manager).authenticate(argThat(authentication ->
                authentication instanceof UsernamePasswordAuthenticationToken
                        && "jane@example.com".equals(authentication.getPrincipal())
                        && "password123".equals(authentication.getCredentials())));
        assertEquals("token-456", response.token());
        assertEquals("jane@example.com", response.user().email());
    }
}
