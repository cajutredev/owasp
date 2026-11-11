package com.demo.service;

import com.demo.entity.Usuario;

public interface AutenticacionService {

    Usuario autenticar(String email, String password);
    Usuario autenticatheXss(String email, String password);

}
