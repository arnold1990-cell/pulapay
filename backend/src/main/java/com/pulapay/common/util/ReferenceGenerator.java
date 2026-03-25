package com.pulapay.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReferenceGenerator {
    public String generate(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
