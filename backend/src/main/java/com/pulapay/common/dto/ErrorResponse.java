package com.pulapay.common.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(boolean success, String message, String path, Instant timestamp, Map<String, String> errors) {
    public static ErrorResponse of(String message, String path, Map<String, String> errors) {
        return new ErrorResponse(false, message, path, Instant.now(), errors);
    }
}
