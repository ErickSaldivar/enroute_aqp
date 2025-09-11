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

            <form action="RegisterServlet" method="POST" class="form">
                <input type="text" name="username" placeholder="Nombre de usuario" required class="input">
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
