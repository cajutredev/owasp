package com.demo.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class PasswordHasherSha256 {

    public PasswordHasherSha256() {
    }

    public static String hash(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(
                    password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Unable to hash password", exception);
        }
    }

    public static boolean verify(String password, String encodedHash) {

        if (password == null || encodedHash == null) {
            return false;
        }

        String calculatedHash = hash(password);

        return MessageDigest.isEqual(
                calculatedHash.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                encodedHash.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}
