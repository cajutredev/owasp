<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.demo.entity.Usuario, java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    if (request.getAttribute("usuarios") == null) {
        request.getRequestDispatcher("/usuarios").forward(request, response);
        return;
    }

    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
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
    <div id="mensaje"></div>
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
                       href="${pageContext.request.contextPath}/usuarios">Limpiar búsqueda</a>
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
                      action="${pageContext.request.contextPath}/usuarios">
                    <label class="visually-hidden" for="user-search">Buscar por nombre</label>
                    <input id="user-search" type="search" name="nombre"
                           value="<c:out value='<%= busquedaNombre %>' />"
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
                    for (Usuario usuario : usuarios) {
                        String userId = String.valueOf(usuario.getId());
                        String deleteFormId = "eliminar-" + userId;
                        String confirmMessage = "¿Seguro que deseas eliminar a "
                                + usuario.getNombre() + " " + usuario.getPrimerApellido() + "?";
                %>
                        <tr data-user-row>
                            <td class="id-cell"><c:out value="<%= userId %>"/></td>
                            <td class="name-cell"><c:out value="<%= usuario.getNombre() %>"/></td>

                            <td><c:out value="<%= usuario.getPrimerApellido() %>"/></td>
                            <td><c:out value="<%= usuario.getEmail() %>"/></td>
                            <td><span class="status-badge <%= usuario.isActivo() ? "is-active" : "is-inactive" %>"><%= usuario.isActivo() ? "Activo" : "Inactivo" %></span></td>
                            <td class="actions-cell">
                                <form id="<%= deleteFormId %>" method="post"
                                      action="${pageContext.request.contextPath}/usuarios"
                                      data-confirm="<%= confirmMessage %>">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= userId %>">
                                </form>
                                <a class="action-button update-button"
                                   href="${pageContext.request.contextPath}/usuarios?action=detail&amp;id=<%= userId %>">Ver detalle</a>
                                <a class="action-button update-button"
                                   href="${pageContext.request.contextPath}/usuarios?action=edit&amp;id=<%= userId %>">Actualizar</a>
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
<script>
const mensaje = decodeURIComponent(
    window.location.hash.substring(1)
);
    if (mensaje) {
        document.getElementById("mensaje").innerHTML = mensaje;
    }
</script>
</html>
