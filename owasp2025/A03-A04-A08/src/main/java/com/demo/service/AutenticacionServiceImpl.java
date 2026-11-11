package com.demo.service;

import com.demo.entity.Usuario;
import com.demo.repository.UsuarioRepository;
import com.demo.security.PasswordHasherSha256;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AutenticacionServiceImpl implements AutenticacionService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario autenticar(String email, String password) {
        String passwordHash = PasswordHasherSha256.hash(password);
        return usuarioRepository.autenticathe(email, passwordHash);
    }

    @Override
    public Usuario autenticatheXss(String email, String password) {

        String passwordHash = PasswordHasherSha256.hash(password);
        return usuarioRepository.autenticatheXss(email, passwordHash);
    }
}
