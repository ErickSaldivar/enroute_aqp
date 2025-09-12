<%-- 
    Document   : register
    Created on : 10 set. 2025, 22:42:44
    Author     : erick
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rutas Arequipa - Registro</title>
    <link rel="stylesheet" href="../css/register.css">
</head>
<body>
<div class="container">
    <header class="header">
        <a href="#" class="logo">
            <img src="../assets/icon_img.png" alt="Rutas Arequipa" class="logo-img">
            <h1 class="logo-title">Rutas Arequipa</h1>
        </a>
    </header>

    <main class="main">
        <div class="form-box">
            <div class="form-header">
                <h2 class="form-title">Crea tu cuenta</h2>
                <p class="form-subtitle">Empieza a planificar tus viajes en autobús por Arequipa.</p>
            </div>
            
            <% String error = request.getParameter("error"); %>
            <% if (error != null) { %>
                <div class="error-message" style="color: #d32f2f; background-color: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center;">
                    <% if ("empty".equals(error)) { %>
                        Todos los campos son obligatorios.
                    <% } else if ("duplicate".equals(error)) { %>
                        Este correo electrónico ya está registrado.
                    <% } else if ("database".equals(error)) { %>
                        Error en la base de datos. Inténtalo de nuevo.
                    <% } else { %>
                        Error desconocido. Inténtalo de nuevo.
                    <% } %>
                </div>
            <% } %>
            
            <% String errorAttr = (String) request.getAttribute("error"); %>
            <% if (errorAttr != null) { %>
                <div class="error-message" style="color: #d32f2f; background-color: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center;">
                    <%= errorAttr %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/RegisterServlet" method="POST" class="form">
                <input type="text" name="nombre" placeholder="Nombres" required class="input">
                <input type="text" name="apellido" placeholder="Apellidos" required class="input">
                <input type="email" name="email" placeholder="Correo electrónico" required class="input">
                <input type="password" name="password" placeholder="Contraseña" required class="input">
                <input type="password" name="confirm-password" placeholder="Confirmar contraseña" required class="input">
                <button type="submit" class="btn">Registrarse</button>
            </form>

            <p class="form-footer">
                ¿Ya tienes una cuenta?
                <a href="login.jsp" class="link">Inicia sesión aquí</a>
            </p>
        </div>
    </main>
</div>
</body>
</html>
