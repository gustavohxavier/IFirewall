package br.com.ifirewall.core.model;

public record Port(int value) {
    public Port {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535.");
        }
    }
}