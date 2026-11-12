package com.demo.security;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped 
public class JakartaPasswordHasher {

    @Inject 
    private Pbkdf2PasswordHash passwordHash;

    @PostConstruct 
    public void init() {
        passwordHash.initialize(Map.of(
            "Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256",
            "Pbkdf2PasswordHash.Iterations", "60000",
            "Pbkdf2PasswordHash.SaltSizeBytes", "16",
            "Pbkdf2PasswordHash.KeySizeBytes", "32"
        ));
    }

    public String hash(String password) {
        return passwordHash.generate(password.toCharArray());
    }

    public boolean veirfy(String password, String storedHahs) {
        if (password == null || storedHahs == null) {
            return false;
        }

        return passwordHash.verify(password.toCharArray(), storedHahs);
    } 
}
