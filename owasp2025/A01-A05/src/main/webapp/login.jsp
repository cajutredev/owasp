<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String loginError = (String) request.getAttribute("loginError");
    String email = (String) request.getAttribute("email");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <main class="login-shell">
        <section class="login-card" aria-labelledby="login-title">
            <p class="eyebrow">Administración</p>
            <h1 id="login-title">Iniciar sesión</h1>
            <p class="login-description">Ingresa tus credenciales para continuar.</p>

            <% if (loginError != null) { %>
                <p class="login-error" role="alert"><%= loginError %></p>
            <% } %>

            <form class="login-form" method="post"
                  action="${pageContext.request.contextPath}/authentication">
                <label class="form-field" for="email">
                    Correo electrónico
                    <input id="email" type="text" name="email" maxlength="350"
                           autocomplete="username" required
                           value="<%= email %>">
                </label>

                <label class="form-field" for="password">
                    Contraseña
                    <input id="password" type="password" name="password" maxlength="128"
                           autocomplete="current-password" required>
                </label>

                <button class="action-button create-button login-submit" type="submit">
                    Iniciar sesión
                </button>
            </form>
        </section>
    </main>
</body>
</html>