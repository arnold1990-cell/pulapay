package com.pulapay.common.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(String message, String path, Instant timestamp, Map<String, String> errors) {
}
