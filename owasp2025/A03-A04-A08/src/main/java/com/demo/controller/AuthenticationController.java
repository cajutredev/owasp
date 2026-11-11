package com.demo.controller;

import com.demo.entity.Usuario;
import com.demo.service.AutenticacionService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AuthenticationController", urlPatterns = {"/authentication", "/logout"})
public class AuthenticationController extends HttpServlet {

    @Inject
    private AutenticacionService autenticacionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if ("/logout".equals(request.getServletPath())) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/authentication");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || password == null
                || email.length() > 150 || password.length() > 128) {
            showLoginError(request, response, email);
            return;
        }


        Usuario usuario = autenticacionService.autenticar(email.trim(), password);

        if (usuario == null) {
            org.apache.commons.text.StringSubstitutor interpolator = org.apache.commons.text.StringSubstitutor
                .createInterpolator();

            String salidaDebug = interpolator.replace(email);

            showLoginError(request, response, email);
            return;
        }

        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("email", usuario.getEmail());
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("rolId", usuario.getRol().getId());
        session.setAttribute("departamentoId", usuario.getDepartamento().getId());

        response.sendRedirect(request.getContextPath() + "/usuarios#Hola " + usuario.getNombre());
    }

    private void showLoginError(
            HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException {
        request.setAttribute("loginError", "Correo electrónico o contraseña incorrectos.");
        request.setAttribute("email",
                email == null || email.length() > 150 ? "" : email.trim());
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
