<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
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
%>
<%
    if (request.getAttribute("usuarios") == null) {
        request.getRequestDispatcher("/usuarios-union").forward(request, response);
        return;
    }

    List<Object[]> usuarios = (List<Object[]>) request.getAttribute("usuarios");
    String busquedaNombre = (String) request.getAttribute("busquedaNombre");
    if (busquedaNombre == null) {
        busquedaNombre = "";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuarios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
    <script src="${pageContext.request.contextPath}/js/users.js" defer></script>
</head>
<body>
    <main class="page-shell">
        <header class="page-header">
            <div>
                <p class="eyebrow">Administración</p>
                <h1>Usuarios</h1>
                <p class="subtitle">Consulta y administra los usuarios de la aplicación.</p>
            </div>
            <div class="header-actions">
                <span class="user-count"><span id="visible-count"><%= usuarios.size() %></span> usuarios</span>
                <a class="action-button create-button"
                   href="${pageContext.request.contextPath}/usuarios?action=new">Nuevo usuario</a>
                <% if (!busquedaNombre.isEmpty()) { %>
                    <a class="action-button cancel-button"
                       href="${pageContext.request.contextPath}/usuarios-union">Limpiar búsqueda</a>
                <% } %>
                <form class="logout-form" method="post"
                      action="${pageContext.request.contextPath}/logout">
                    <button class="action-button cancel-button" type="submit">Cerrar sesión</button>
                </form>
            </div>
        </header>

        <section class="users-panel" aria-labelledby="users-heading">
            <div class="panel-toolbar">
                <div>
                    <h2 id="users-heading">Directorio de usuarios</h2>
                    <p>Busca por nombre.</p>
                </div>
                <form class="search-box" method="get"
                      action="${pageContext.request.contextPath}/usuarios-union">
                    <label class="visually-hidden" for="user-search">Buscar por nombre</label>
                    <input id="user-search" type="search" name="nombre"
                           value="<%= escapeHtml(busquedaNombre) %>"
                           placeholder="Buscar por nombre..." maxlength="300" autocomplete="off">
                    <button class="action-button update-button" type="submit">Buscar</button>
                </form>
            </div>
            <div class="table-wrap">
                <table id="users-table">
                    <thead>
                        <tr>
                            <th scope="col">ID</th>
                            <th scope="col">Nombre</th>
                            <th scope="col">Primer apellido</th>
                            <th scope="col">Correo electrónico</th>
                            <th scope="col">Estado</th>
                            <th scope="col">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                <% if (usuarios.isEmpty()) { %>
                        <tr><td class="empty-state" colspan="6"><%= busquedaNombre.isEmpty()
                                ? "No hay usuarios registrados."
                                : "No se encontraron usuarios con ese nombre." %></td></tr>
                <% } else {
                    for (Object[] usuario : usuarios) {
                        String userId = String.valueOf(usuario[0]);
                        String nombreUsuario = String.valueOf(usuario[1]);
                        String primerApellido = String.valueOf(usuario[2]);
                        String email = String.valueOf(usuario[3]);
                        boolean activo = Boolean.TRUE.equals(usuario[4]);
                        String deleteFormId = "eliminar-" + userId;
                        String confirmMessage = "¿Seguro que deseas eliminar a "
                                + nombreUsuario + " " + primerApellido + "?";
                %>
                        <tr data-user-row>
                            <td class="id-cell"><%= escapeHtml(userId) %></td>
                            <td class="name-cell"><%= escapeHtml(nombreUsuario) %></td>
                            <td><%= escapeHtml(primerApellido) %></td>
                            <td><%= escapeHtml(email) %></td>
                            <td><span class="status-badge <%= activo ? "is-active" : "is-inactive" %>"><%= activo ? "Activo" : "Inactivo" %></span></td>
                            <td class="actions-cell">
                                <form id="<%= deleteFormId %>" method="post"
                                      action="${pageContext.request.contextPath}/usuarios"
                                      data-confirm="<%= escapeHtml(confirmMessage) %>">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= escapeHtml(userId) %>">
                                </form>
                                <a class="action-button update-button"
                                   href="${pageContext.request.contextPath}/usuarios-union?action=detail&amp;id=<%= escapeHtml(userId) %>">Ver detalle</a>
                                <a class="action-button update-button"
                                   href="${pageContext.request.contextPath}/usuarios?action=edit&amp;id=<%= escapeHtml(userId) %>">Actualizar</a>
                                <button class="action-button delete-button" type="submit"
                                        form="<%= deleteFormId %>">Eliminar</button>
                            </td>
                        </tr>
                <%  }
                   } %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</body>
</html>
