package com.pulapay.auth;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.AuthResponse;
import com.pulapay.auth.dto.LoginRequest;
import com.pulapay.auth.service.AuthService;
import com.pulapay.auth.service.LocalAdminCredentialsInitializer;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocalAdminCredentialsInitializerTest {
    @Test
    void syncUsesApplicationPasswordEncoderAndEnablesSuccessfulLogin() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoded = passwordEncoder.encode("admin123");
        assertTrue(passwordEncoder.matches("admin123", encoded));

        UserRepository userRepository = mock(UserRepository.class);
        User admin = new User();
        admin.setFullName("PulaPay Admin");
        admin.setEmail("admin@pulapay.com");
        admin.setPhoneNumber("26770000000");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setPassword("stale-hash");

        when(userRepository.findByEmail("admin@pulapay.com")).thenReturn(Optional.of(admin));
        doAnswer(invocation -> invocation.getArgument(0)).when(userRepository).save(any(User.class));

        LocalAdminCredentialsInitializer initializer = new LocalAdminCredentialsInitializer(userRepository, passwordEncoder);
        initializer.run(new DefaultApplicationArguments(new String[]{}));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertNotEquals("stale-hash", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("admin123", savedUser.getPassword()));

        WalletRepository walletRepository = mock(WalletRepository.class);
        JwtService jwtService = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService auditLogService = mock(AuditLogService.class);

        when(userRepository.findByEmail("admin@pulapay.com")).thenReturn(Optional.of(savedUser));
        when(jwtService.generateToken("admin@pulapay.com")).thenReturn("admin-token");

        AuthService authService = new AuthService(
                userRepository,
                walletRepository,
                passwordEncoder,
                jwtService,
                walletNumberGenerator,
                auditLogService
        );

        AuthResponse response = authService.login(new LoginRequest("admin@pulapay.com", "admin123"));

        assertEquals("admin-token", response.token());
        assertEquals("admin@pulapay.com", response.user().email());
        assertEquals(Role.ADMIN, response.user().role());
    }
}
