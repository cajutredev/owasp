package com.demo.security;

import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class CsrfToken {

    private static final String SESSION_ATTRIBUTE = CsrfToken.class.getName() + ".token";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CsrfToken() {
    }

    public static String getOrCreateToken(HttpSession session) {
        synchronized (session) {
            Object existingToken = session.getAttribute(SESSION_ATTRIBUTE);
            if (existingToken instanceof String token) {
                return token;
            }

            byte[] randomBytes = new byte[32];
            SECURE_RANDOM.nextBytes(randomBytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            session.setAttribute(SESSION_ATTRIBUTE, token);
            return token;
        }
    }

    public static boolean validateToken(HttpSession session, String providedToken) {
        if (session == null || providedToken == null) {
            return false;
        }

        Object expectedToken = session.getAttribute(SESSION_ATTRIBUTE);
        if (!(expectedToken instanceof String token) || token.length() != providedToken.length()) {
            return false;
        }

        return MessageDigest.isEqual(
                token.getBytes(StandardCharsets.US_ASCII),
                providedToken.getBytes(StandardCharsets.US_ASCII));
    }
}
