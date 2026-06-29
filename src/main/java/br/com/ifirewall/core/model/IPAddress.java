package br.com.ifirewall.core.model;

import java.util.regex.Pattern;

public record IPAddress(String value) {
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public IPAddress {
        if (value == null || !IPV4_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid IPv4 address format: " + value);
        }
    }
}