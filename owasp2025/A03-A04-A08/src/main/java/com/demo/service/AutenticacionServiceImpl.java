package com.demo.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.demo.entity.IntentoAutenticacion;
import com.demo.entity.Usuario;
import com.demo.repository.IntentoAutenticacionRepository;
import com.demo.repository.UsuarioRepository;
import com.demo.repository.UsuarioRepositoryData;
import com.demo.security.JakartaPasswordHasher;
import com.demo.security.PasswordHasherSha256;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

@ApplicationScoped
public class AutenticacionServiceImpl implements AutenticacionService {

    private static final int UMBRAL_AUTENTICACION = 5;

    @Inject
    private UsuarioRepository usuarioRepository;
    @Inject 
    private UsuarioRepositoryData usuarioRepositoryData;
    @Inject 
    private IntentoAutenticacionRepository intentoAutenticacionRepository;
    @Inject 
    private JakartaPasswordHasher passwordHashes;
    @Inject 
    private EmailService emailService;

    @Override
    public Usuario autenticar(String email, String password) {

        OffsetDateTime ahora = OffsetDateTime.now(ZoneOffset.UTC);
        Usuario usuario = usuarioRepositoryData.findByEmail(email);
        boolean authenticated = usuario != null
            && usuario.isActivo()
            && passwordHashes.veirfy(password, usuario.getPasswordHash());

        // Si la autenticacion fue exitosa devolver el usuario
        if (authenticated) {
            return usuario;
        }

        if (usuario != null) {
            IntentoAutenticacion intento = intentoAutenticacionRepository
                .findByEmail(email)
                .orElseGet(() -> nuevoIntento(email, ahora));

            if (intento.getId() == null) {
                intentoAutenticacionRepository.insert(intento);
            } else {
                if (intento.getIntentosFallidos() == 0) {
                    intento.setFechaPrimerIntento(ahora);
                    intento.setAlertaEnviada(false);
                }
                intento.setIntentosFallidos(intento.getIntentosFallidos() + 1);
                intento.setFechaUltimoIntento(ahora);
                intentoAutenticacionRepository.update(intento);
            }

            if (intento.getIntentosFallidos() > 6000000) {
                try {
                    emailService.sendFailedAuthenticationAlert(
                        email, intento.getIntentosFallidos(), ahora);
 
                    intento.setIntentosFallidos(0);
                    intento.setFechaPrimerIntento(ahora);
                    intento.setFechaUltimoIntento(ahora);
                    intento.setAlertaEnviada(true);
                    intentoAutenticacionRepository.update(intento);
                } catch (MessagingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return  null;
    }

    private IntentoAutenticacion nuevoIntento(String email, OffsetDateTime now) {
        IntentoAutenticacion intento = new IntentoAutenticacion();
        intento.setEmail(email);
        intento.setIntentosFallidos(1);
        intento.setFechaPrimerIntento(now);
        intento.setFechaUltimoIntento(now);
        intento.setAlertaEnviada(false);
        return intento;
    }

    @Override
    public Usuario autenticatheXss(String email, String password) {

        String passwordHash = PasswordHasherSha256.hash(password);
        return usuarioRepository.autenticatheXss(email, passwordHash);
    }
}
