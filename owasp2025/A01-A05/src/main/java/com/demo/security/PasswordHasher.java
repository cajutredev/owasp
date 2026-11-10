package com.demo.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {

    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH_BYTES = 16;
    private static final int HASH_LENGTH_BITS = 256;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String hash(String password) {
        char[] passwordChars = password.toCharArray();
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(salt);

        PBEKeySpec keySpec = new PBEKeySpec(passwordChars, salt, ITERATIONS, HASH_LENGTH_BITS);
        try {
            byte[] derivedKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(keySpec)
                    .getEncoded();
            Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
            return "pbkdf2-sha256$" + ITERATIONS + "$"
                    + encoder.encodeToString(salt) + "$"
                    + encoder.encodeToString(derivedKey);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to hash password", exception);
        } finally {
            keySpec.clearPassword();
            java.util.Arrays.fill(passwordChars, '\0');
        }
    }

    public static boolean verify(String password, String encodedHash) {
        if (password == null || encodedHash == null) {
            return false;
        }

        String[] parts = encodedHash.split("\\$", -1);
        if (parts.length != 4 || !"pbkdf2-sha256".equals(parts[0])) {
            return false;
        }

        int iterations;
        byte[] salt;
        byte[] expectedHash;
        try {
            iterations = Integer.parseInt(parts[1]);
            salt = Base64.getUrlDecoder().decode(parts[2]);
            expectedHash = Base64.getUrlDecoder().decode(parts[3]);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        if (iterations != ITERATIONS
                || salt.length != SALT_LENGTH_BYTES
                || expectedHash.length != HASH_LENGTH_BITS / Byte.SIZE) {
            return false;
        }

        char[] passwordChars = password.toCharArray();
        PBEKeySpec keySpec = new PBEKeySpec(
                passwordChars, salt, iterations, HASH_LENGTH_BITS);
        try {
            byte[] actualHash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(keySpec)
                    .getEncoded();
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to verify password", exception);
        } finally {
            keySpec.clearPassword();
            Arrays.fill(passwordChars, '\0');
        }
    }
}
