<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.demo.entity.Usuario, com.demo.entity.Departamento, com.demo.entity.Rol" %>
<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    private String displayValue(Object value) {
        if (value == null || value.toString().isBlank()) {
            return "No registrado";
        }
        return escapeHtml(value.toString());
    }
%>
<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    if (usuario == null) {
        request.getRequestDispatcher("/usuarios").forward(request, response);
        return;
    }

    String returnPath = (String) request.getAttribute("returnPath");
    if (!"/usuarios".equals(returnPath) && !"/usuarios-union".equals(returnPath)) {
        returnPath = "/usuarios";
    }
    Departamento departamento = usuario.getDepartamento();
    Rol rol = usuario.getRol();
    String userId = String.valueOf(usuario.getId());
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalle del usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
</head>
<body>
    <main class="page-shell">
        <header class="page-header">
            <div>
                <p class="eyebrow">Administración</p>
                <h1>Detalle del usuario</h1>
                <p class="subtitle">Información completa de la cuenta seleccionada.</p>
            </div>
            <div class="header-actions">
                <a class="action-button cancel-button"
                   href="${pageContext.request.contextPath}<%= returnPath %>">Volver a usuarios</a>
                <a class="action-button create-button"
                   href="${pageContext.request.contextPath}<%= returnPath %>?action=edit&amp;id=<%= escapeHtml(userId) %>">Actualizar</a>
                <form class="logout-form" method="post"
                      action="${pageContext.request.contextPath}/logout">
                    <button class="action-button cancel-button" type="submit">Cerrar sesión</button>
                </form>
            </div>
        </header>

        <section class="detail-panel" aria-labelledby="detail-heading">
            <div class="detail-heading">
                <div>
                    <h2 id="detail-heading"><%= displayValue(usuario.getNombre()) %>
                        <%= displayValue(usuario.getPrimerApellido()) %></h2>
                    <p>Identificador: <%= displayValue(usuario.getId()) %></p>
                </div>
                <span class="status-badge <%= usuario.isActivo() ? "is-active" : "is-inactive" %>">
                    <%= usuario.isActivo() ? "Activo" : "Inactivo" %>
                </span>
            </div>

            <dl class="detail-grid">
                <div class="detail-field">
                    <dt>Nombre</dt>
                    <dd><%= displayValue(usuario.getNombre()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Primer apellido</dt>
                    <dd><%= displayValue(usuario.getPrimerApellido()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Segundo apellido</dt>
                    <dd><%= displayValue(usuario.getSegundoApellido()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Correo electrónico</dt>
                    <dd><%= displayValue(usuario.getEmail()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Contraseña</dt>
                    <dd>No se muestra por seguridad</dd>
                </div>
                <div class="detail-field">
                    <dt>Teléfono</dt>
                    <dd><%= displayValue(usuario.getTelefono()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Fecha de nacimiento</dt>
                    <dd><%= displayValue(usuario.getFechaNacimiento()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Fecha de creación</dt>
                    <dd><%= displayValue(usuario.getFechaCreacion()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Última actualización</dt>
                    <dd><%= displayValue(usuario.getFechaActualizacion()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Departamento</dt>
                    <dd><%= departamento == null ? "No registrado"
                            : displayValue(departamento.getId() + " - " + departamento.getNombre()) %></dd>
                </div>
                <div class="detail-field">
                    <dt>Rol</dt>
                    <dd><%= rol == null ? "No registrado"
                            : displayValue(rol.getId() + " - " + rol.getNombre()) %></dd>
                </div>
            </dl>
        </section>
    </main>
</body>
</html>