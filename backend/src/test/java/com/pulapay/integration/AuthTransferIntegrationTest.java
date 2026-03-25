package com.pulapay.integration;

import com.pulapay.config.JwtAuthenticationFilter;
import com.pulapay.config.JwtService;
import com.pulapay.user.entity.Role;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class AuthTransferIntegrationTest {
    private JwtService jwtService;
    private UserRepository userRepository;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userRepository = mock(UserRepository.class);
        filter = new JwtAuthenticationFilter(jwtService, userRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsAuthenticationWhenBearerTokenIsValid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        User user = new User();
        user.setEmail("jane@example.com");
        user.setPhoneNumber("SYS123");
        user.setRole(Role.USER);

        when(jwtService.extractUsername("valid-token")).thenReturn("jane@example.com");
        when(jwtService.validateToken("valid-token")).thenReturn(true);
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        filter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void leavesRequestUnauthenticatedWithoutBearerToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
