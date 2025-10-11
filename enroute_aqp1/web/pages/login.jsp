<%-- 
    Document   : register
    Created on : 10 set. 2025, 22:42:44
    Author     : erick
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Rutas Arequipa</title>
    <link rel="stylesheet" href="../css/login.css">
</head>
<body class="bg">

    <div class="container">
        <div class="logo-box">
            <img src="../img/icon_img.png" alt="alt" width="60" height="60"/>
            <h1 class="title">Rutas Arequipa</h1>
        </div>

        <div class="card">
            <h2 class="subtitle">Iniciar Sesión</h2>
            
            <% String error = (String) request.getAttribute("error"); %>
             <% if (error != null) { %>
                 <div class="error-message" style="color: #d32f2f; background-color: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center;">
                     <%= error %>
                 </div>
             <% } %>
             
             <% String registration = request.getParameter("registration"); %>
             <% if ("success".equals(registration)) { %>
                 <div class="success-message" style="color: #2e7d32; background-color: #e8f5e8; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center;">
                     ¡Registro exitoso! Ahora puedes iniciar sesión.
                 </div>
             <% } %>
            
            <form method="post" action="${pageContext.request.contextPath}/LoginServlet" class="form">
                <div class="form-group">
                    <label for="email">Correo electrónico</label>
                    <input type="email" id="email" name="email" placeholder="correo@ejemplo.com" required>
                </div>
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" required>
                </div>
                <div class="form-options">
                    <a href="#">¿Olvidaste tu contraseña?</a>
                </div>
                <button type="submit" class="btn">Iniciar Sesión</button>
            </form>

            <p class="footer-text">¿No tienes una cuenta? <a href="register.jsp">Crear cuenta</a></p>
        </div>
    </div>

</body>
</html>
