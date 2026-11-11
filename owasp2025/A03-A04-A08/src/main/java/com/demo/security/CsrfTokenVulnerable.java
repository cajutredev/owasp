package com.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public final class CsrfTokenVulnerable {

    public static String generateToken(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email requerido");
        }

        return Base64.getEncoder().encodeToString(
            email.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static boolean validateToken(
            String email,
            String providedToken) {

        if (email == null || providedToken == null) {
            return false;
        }

        String expectedToken = generateToken(email);

        return MessageDigest.isEqual(
            expectedToken.getBytes(StandardCharsets.UTF_8),
            providedToken.getBytes(StandardCharsets.UTF_8)
        );
    }
}