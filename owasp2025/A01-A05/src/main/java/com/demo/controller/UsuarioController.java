package com.demo.controller;

import com.demo.entity.Usuario;
import com.demo.security.PasswordHasherSha256;
import com.demo.security.CsrfToken;
import com.demo.security.CsrfTokenVulnerable;
import com.demo.service.DepartamentoService;
import com.demo.service.RolService;
import com.demo.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsuarioController", urlPatterns = "/usuarios")
public class UsuarioController extends HttpServlet {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private DepartamentoService departamentoService;

    @Inject
    private RolService rolService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        if ("new".equals(request.getParameter("action"))) {
            forwardToUserForm(request, response, null);
            return;
        }

        if ("detail".equals(request.getParameter("action"))) {
            try {
                Usuario usuario = usuarioService.findById(parseId(request, "id"));
                if (usuario == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                request.setAttribute("usuario", usuario);
                request.setAttribute("returnPath", request.getServletPath());
                request.getRequestDispatcher("/detail.jsp").forward(request, response);
            } catch (IllegalArgumentException exception) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
            }
            return;
        }

        if ("edit".equals(request.getParameter("action"))) {
            try {
                Usuario usuario = usuarioService.findById(parseId(request, "id"));
                if (usuario == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
                forwardToUserForm(request, response, usuario);
            } catch (IllegalArgumentException exception) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
            }
            return;
        }

        String nombre;
        try {
            nombre = request.getParameter("nombre");
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
            return;
        }

        List<Usuario> usuarios = nombre != null
                //? usuarioService.findByName(nombre)
                ? usuarioService.findByNameXss(nombre)
                : usuarioService.findAll();
        request.setAttribute("busquedaNombre", nombre == null ? "" : nombre);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void forwardToUserForm(
            HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        
        request.setAttribute("usuario", usuario);
        if (usuario != null && request.getParameter("csrf") != null) {
            //request.setAttribute("csrfToken", CsrfTokenVulnerable.generateToken(
            request.setAttribute("csrfToken", CsrfToken.getOrCreateToken(request.getSession()));
        }
        request.setAttribute("departamentos", departamentoService.findAll());
        request.setAttribute("roles", rolService.findAll());
        request.getRequestDispatcher("/form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String action = required(request, "action", 20);
            if ("update".equals(action)
                    && request.getParameter("csrf") != null
                    && !CsrfTokenVulnerable.validateToken(
                            request.getSession().getAttribute("email").toString(), 
                            request.getParameter("csrf"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }

            switch (action) {
                case "create" -> createUser(request);
                case "update" -> updateUser(request);
                case "delete" -> usuarioService.delete(parseId(request, "id"));
                default -> throw new IllegalArgumentException("Invalid action");
            }
            response.sendRedirect(request.getContextPath() + "/usuarios");
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        }
    }

    private void createUser(HttpServletRequest request) {
        String password = password(request);
        Long departmentId = parseId(request, "departamentoId");
        Long roleId = parseId(request, "rolId");
        validateReferences(departmentId, roleId);

        //usuarioService.create(
        usuarioService.createXss(
                required(request, "nombre", 100),
                required(request, "primerApellido", 100),
                optional(request, "segundoApellido", 100),
                email(request),
                PasswordHasherSha256.hash(password),
                optional(request, "telefono", 20),
                departmentId,
                roleId);
    }

    private void updateUser(HttpServletRequest request) {
        if (request.getParameter("partial") != null) {
            updatePartialUser(request);
            return;
        }

        Long departmentId = parseId(request, "departamentoId");
        Long roleId = parseId(request, "rolId");
        validateReferences(departmentId, roleId);

        usuarioService.update(
                parseId(request, "id"),
                required(request, "nombre", 100),
                required(request, "primerApellido", 100),
                optional(request, "segundoApellido", 100),
                email(request),
                optional(request, "telefono", 20),
                departmentId,
                roleId);
    }

    private void updatePartialUser(HttpServletRequest request) {
        Long id = parseId(request, "id");
        String nombre = request.getParameter("nombre") == null
                ? null : required(request, "nombre", 100);
        String primerApellido = request.getParameter("primerApellido") == null
                ? null : required(request, "primerApellido", 100);
        String segundoApellido = partialOptional(request, "segundoApellido", 100);
        String email = request.getParameter("email") == null ? null : email(request);
        String telefono = partialOptional(request, "telefono", 20);

        Long departmentId = request.getParameter("departamentoId") == null
                ? null : parseId(request, "departamentoId");
        Long roleId = request.getParameter("rolId") == null
                ? null : parseId(request, "rolId");
        if (departmentId != null && departamentoService.findById(departmentId) == null) {
            throw new IllegalArgumentException("Select an existing department");
        }
        if (roleId != null && rolService.findById(roleId) == null) {
            throw new IllegalArgumentException("Select an existing role");
        }

        usuarioService.updatePartial(
                id,
                nombre,
                primerApellido,
                segundoApellido,
                email,
                telefono,
                departmentId,
                roleId);
    }

    private String email(HttpServletRequest request) {
        String email = required(request, "email", 150);
        if (email.chars().anyMatch(Character::isWhitespace)
                || !email.matches("[^@]+@[^@]+\\.[^@]+")) {
            throw new IllegalArgumentException("Enter a valid email address");
        }
        return email;
    }

    private String password(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (password.length() < 12 || password.length() > 128) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener entre 12 y 128 caracteres");
        }
        return password;
    }

    private String required(HttpServletRequest request, String name, int maxLength) {
        String value = request.getParameter(name);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + name);
        }
        value = value.trim();
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("Field is too long: " + name);
        }
        rejectMarkupCharacters(value);
        return value;
    }

    private String optional(HttpServletRequest request, String name, int maxLength) {
        String value = request.getParameter(name);
        if (value == null || value.isBlank()) {
            return null;
        }
        value = value.trim();
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("Field is too long: " + name);
        }
        rejectMarkupCharacters(value);
        return value;
    }

    private String partialOptional(HttpServletRequest request, String name, int maxLength) {
        String value = request.getParameter(name);
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() > maxLength) {
            throw new IllegalArgumentException("Field is too long: " + name);
        }
        rejectMarkupCharacters(value);
        return value;
    }

    private Long parseId(HttpServletRequest request, String name) {
        String value = required(request, name, 19);
        try {
            long id = Long.parseLong(value);
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid identifier: " + name);
            }
            return id;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid identifier: " + name);
        }
    }

    private void validateReferences(Long departmentId, Long roleId) {
        if (departamentoService.findById(departmentId) == null
                || rolService.findById(roleId) == null) {
            throw new IllegalArgumentException("Select an existing department and role");
        }
    }

    private void rejectMarkupCharacters(String value) {
        if (value.indexOf("<") >= 0 || value.indexOf(">") >= 0) {
            System.out.println("La cadena viene con caracteres raros");
        }
    }
}
