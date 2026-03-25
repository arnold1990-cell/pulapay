package com.pulapay.common.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class WalletNumberGenerator {
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder builder = new StringBuilder("26");
        for (int i = 0; i < 8; i++) builder.append(random.nextInt(10));
        return builder.toString();
    }
}
