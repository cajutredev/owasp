package com.demo.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger(AutenticacionServiceImpl.class);

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

        LOGGER.info("Intento de autenticacion para la cuenta={}", email);

        OffsetDateTime ahora = OffsetDateTime.now(ZoneOffset.UTC);
        Usuario usuario = usuarioRepositoryData.findByEmail(email);
        boolean authenticated = usuario != null
                && usuario.isActivo()
                && passwordHashes.veirfy(password, usuario.getPasswordHash());

        // Si la autenticacion fue exitosa devolver el usuario
        if (authenticated) {
            LOGGER.info("Autenticacion exitosa para la cuenta={}", email);
            return usuario;
        }

        if (usuario != null) {

            LOGGER.warn("Intento de autenticacion fallida para la cuenta={}", email);
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

            LOGGER.warn("Se actualiza intento de autenticacion para la cuenta={}", email);

            if (intento.getIntentosFallidos() > UMBRAL_AUTENTICACION) {
                LOGGER.warn("Autenticacion fallida, se manda alerta para la cuenta={} con numero de intentos {}", 
                                email, intento.getIntentosFallidos());
                try {
                    emailService.sendFailedAuthenticationAlert(
                            email, intento.getIntentosFallidos(), ahora);

                    intento.setIntentosFallidos(0);
                    intento.setFechaPrimerIntento(ahora);
                    intento.setFechaUltimoIntento(ahora);
                    intento.setAlertaEnviada(true);
                    intentoAutenticacionRepository.update(intento);
                } catch (MessagingException e) {
                    LOGGER.error(
                        "Fallo el envio de correo electronico para la cuenta={}, tipo de error={}, mensaje={}",
                        email, e.getClass().getSimpleName(), e.getMessage());
                }
            }
        }

        return null;
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
