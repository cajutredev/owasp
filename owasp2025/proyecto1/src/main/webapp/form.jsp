<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.demo.entity.Departamento, com.demo.entity.Rol, com.demo.entity.Usuario, java.util.List" %>

<%
    List<Departamento> departamentos =
            (List<Departamento>) request.getAttribute("departamentos");
    List<Rol> roles = (List<Rol>) request.getAttribute("roles");
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    boolean editing = usuario != null;
    if (departamentos == null || roles == null) {
        response.sendRedirect(request.getContextPath() + "/usuarios?action=new");
        return;
    }
    String departmentId = editing
            ? String.valueOf(usuario.getDepartamento().getId()) : "";
    String roleId = editing ? String.valueOf(usuario.getRol().getId()) : "";
    String csrfToken = (String) request.getAttribute("csrfToken");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= editing ? "Actualizar usuario" : "Registrar usuario" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
</head>
<body>
    <main class="page-shell">
        <header class="page-header">
            <div>
                <p class="eyebrow">Administración</p>
                <h1><%= editing ? "Actualizar usuario" : "Registrar usuario" %></h1>
                <p class="subtitle"><%= editing
                        ? "Modifica los datos del usuario."
                        : "Completa los datos para agregar un usuario." %></p>
            </div>
            <form class="logout-form" method="post"
                  action="${pageContext.request.contextPath}/logout">
                <button class="action-button cancel-button" type="submit">Cerrar sesión</button>
            </form>
        </header>

        <section class="form-panel"
                 aria-label="<%= editing ? "Formulario de actualización de usuario" : "Formulario de registro de usuario" %>">
            <form class="user-form" method="post"
                  action="${pageContext.request.contextPath}/usuarios">
                <input type="hidden" name="action" value="<%= editing ? "update" : "create" %>">
                <% if (editing) { %>
                    <input type="hidden" name="id"
                           value="<%= String.valueOf(usuario.getId()) %>">
                    <% if (csrfToken != null) { %>
                        <input type="hidden" name="csrf" value="<%= csrfToken %>">
                    <% } %>
                <% } %>

                <label class="form-field">
                    Nombre
                    <input type="text" name="nombre" maxlength="100" required
                           value="<%= editing ? usuario.getNombre() : "" %>">
                </label>

                <label class="form-field">
                    Primer apellido
                    <input type="text" name="primerApellido" maxlength="100" required
                           value="<%= editing ? usuario.getPrimerApellido() : "" %>">
                </label>

                <label class="form-field">
                    Segundo apellido
                    <input type="text" name="segundoApellido" maxlength="100"
                           value="<%= editing ? usuario.getSegundoApellido() : "" %>">
                </label>

                <label class="form-field">
                    Correo electrónico
                    <input type="email" name="email" maxlength="150" autocomplete="email" required
                           value="<%= editing ? usuario.getEmail() : "" %>">
                </label>

                <% if (!editing) { %>
                    <label class="form-field">
                        Contraseña
                        <input type="password" name="password" minlength="12" maxlength="128"
                               autocomplete="new-password" required>
                        <span class="field-help">Usa entre 12 y 128 caracteres. Se guardará un hash seguro, no la contraseña.</span>
                    </label>
                <% } %>

                <label class="form-field">
                    Teléfono
                    <input type="tel" name="telefono" maxlength="20" autocomplete="tel"
                           value="<%= editing ? usuario.getTelefono() : "" %>">
                </label>

                <label class="form-field">
                    Departamento
                    <select name="departamentoId" required>
                        <option value="" <%= editing ? "" : "selected" %> disabled>
                            Selecciona un departamento
                        </option>
                        <% for (Departamento departamento : departamentos) { %>
                            <% String optionId = String.valueOf(departamento.getId()); %>
                            <option value="<%= optionId %>"
                                    <%= optionId.equals(departmentId) ? "selected" : "" %>>
                                <%= departamento.getNombre() %>
                            </option>
                        <% } %>
                    </select>
                </label>

                <label class="form-field">
                    Rol
                    <select name="rolId" required>
                        <option value="" <%= editing ? "" : "selected" %> disabled>
                            Selecciona un rol
                        </option>
                        <% for (Rol rol : roles) { %>
                            <% String optionId = String.valueOf(rol.getId()); %>
                            <option value="<%= optionId %>"
                                    <%= optionId.equals(roleId) ? "selected" : "" %>>
                                <%= rol.getNombre() %>
                            </option>
                        <% } %>
                    </select>
                </label>

                <div class="form-actions">
                    <button class="action-button create-button" type="submit">
                        <%= editing ? "Guardar cambios" : "Guardar usuario" %>
                    </button>
                    <a class="action-button cancel-button"
                       href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
                </div>
            </form>
        </section>
    </main>
</body>
</html>