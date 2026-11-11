package com.demo.security;



import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordHasher256Salt {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 16;


    // Generar hash de contraseña
    public static String hash(String password) {
        try {
            // Generar salt aleatorio
            byte[] salt = new byte[SALT_LENGTH];
            RANDOM.nextBytes(salt);

            // Calcular SHA-256 de contraseña + salt
            byte[] hash = calculateHash(password, salt);

            // Almacenar salt y hash
            Base64.Encoder encoder = Base64.getEncoder();

            return encoder.encodeToString(salt) + "$"
                    + encoder.encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Error generating password hash", e);
        }
    }

    // Verificar contraseña
    public static boolean verify(String password, String storedHash) {
        try {
            if (password == null || storedHash == null) {
                return false;
            }

            String[] parts = storedHash.split("\\$", -1);

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            if (salt.length != SALT_LENGTH || expectedHash.length != 32) {
                return false;
            }

            byte[] actualHash = calculateHash(password, salt);

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        }
    }

    // Calcular SHA-256
    private static byte[] calculateHash(String password, byte[] salt)
            throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.update(password.getBytes(StandardCharsets.UTF_8));
        digest.update(salt);

        return digest.digest();
    }
}

