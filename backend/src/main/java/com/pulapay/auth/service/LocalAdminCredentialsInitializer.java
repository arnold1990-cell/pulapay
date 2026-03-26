package com.pulapay.auth.service;

import com.pulapay.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
@ConditionalOnProperty(prefix = "app.seed.local-admin", name = "enabled", havingValue = "true")
public class LocalAdminCredentialsInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(LocalAdminCredentialsInitializer.class);
    private static final String ADMIN_EMAIL = "admin@pulapay.com";
    private static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalAdminCredentialsInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        userRepository.findByEmail(ADMIN_EMAIL.toLowerCase(Locale.ROOT)).ifPresent(adminUser -> {
            boolean changed = false;

            if (!passwordEncoder.matches(ADMIN_PASSWORD, adminUser.getPassword())) {
                adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                changed = true;
            }

            if (!adminUser.isActive()) {
                adminUser.setActive(true);
                changed = true;
            }

            if (changed) {
                userRepository.save(adminUser);
            }

            log.info("Ensured local admin credentials for {}", ADMIN_EMAIL);
        });
    }
}
