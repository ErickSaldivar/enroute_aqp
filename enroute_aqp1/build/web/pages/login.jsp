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
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex justify-content-center align-items-center min-vh-100" style="background-color: #F5EFE6; font-family: Arial, sans-serif;">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <!-- Logo Section -->
                <div class="text-center mb-4">
                    <img src="../img/icon_img.png" alt="Logo" width="60" height="60" class="mb-2"/>
                    <h1 class="h3 fw-bold" style="color: #2D3748;">Rutas Arequipa</h1>
                </div>

                <!-- Login Card -->
                <div class="card shadow-sm border-0 rounded-4" style="background: rgba(255, 255, 255, 0.95);">
                    <div class="card-body p-4">
                        <h2 class="h4 fw-bold mb-4 text-dark">Iniciar Sesión</h2>
                        
                        <!-- Error Messages -->
                        <% String error = (String) request.getAttribute("error"); %>
                        <% if (error != null) { %>
                            <div class="alert alert-danger text-center mb-3" role="alert">
                                <%= error %>
                            </div>
                        <% } %>
                        
                        <!-- Success Messages -->
                        <% String registration = request.getParameter("registration"); %>
                        <% if ("success".equals(registration)) { %>
                            <div class="alert alert-success text-center mb-3" role="alert">
                                ¡Registro exitoso! Ahora puedes iniciar sesión.
                            </div>
                        <% } %>
                        
                        <!-- Login Form -->
                        <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
                            <div class="mb-3">
                                <label for="email" class="form-label fw-medium text-dark">Correo electrónico</label>
                                <input type="email" class="form-control form-control-lg border-1 rounded-3" 
                                       id="email" name="email" placeholder="correo@ejemplo.com" 
                                       style="background-color: #FFFFFF; color: #2D3748; border-color: #e2e8f0;" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label fw-medium text-dark">Contraseña</label>
                                <input type="password" class="form-control form-control-lg border-1 rounded-3" 
                                       id="password" name="password" placeholder="••••••••" 
                                       style="background-color: #FFFFFF; color: #2D3748; border-color: #e2e8f0;" required>
                            </div>
                            
                            <div class="text-end mb-3">
                                <a href="#" class="text-decoration-none small" style="color: #DD6B20;">¿Olvidaste tu contraseña?</a>
                            </div>
                            
                            <button type="submit" class="btn btn-lg w-100 fw-bold rounded-3 mt-3" 
                                    style="background-color: #DD6B20; color: #FFFFFF; border: none;">
                                Iniciar Sesión
                            </button>
                        </form>

                        <p class="text-center mt-4 mb-0" style="color: #718096;">
                            ¿No tienes una cuenta? 
                            <a href="register.jsp" class="text-decoration-none fw-bold" style="color: #DD6B20;">Crear cuenta</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
