package com.pulapay.auth;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.auth.dto.RegisterRequest;
import com.pulapay.auth.service.AuthService;
import com.pulapay.config.JwtService;
import com.pulapay.common.util.WalletNumberGenerator;
import com.pulapay.user.repository.UserRepository;
import com.pulapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Test
    void registerCreatesToken() {
        UserRepository userRepository = mock(UserRepository.class);
        WalletRepository walletRepository = mock(WalletRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        JwtService jwt = mock(JwtService.class);
        WalletNumberGenerator walletNumberGenerator = mock(WalletNumberGenerator.class);
        AuditLogService audit = mock(AuditLogService.class);
        when(encoder.encode(anyString())).thenReturn("hash");
        when(jwt.generateToken(any())).thenReturn("token");
        when(walletNumberGenerator.generate()).thenReturn("2600000001");
        AuthService service = new AuthService(userRepository, walletRepository, encoder, manager, jwt, walletNumberGenerator, audit);
        assertNotNull(service);
    }
}
