<%-- 
    Document   : register
    Created on : 10 oct. 2025, 09:03:42
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rutas Arequipa - Registro</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100" style="background-color: #2D3748; font-family: Arial, sans-serif;">

    <!-- Header -->
    <header class="border-bottom py-3" style="border-color: #4A5568 !important;">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-auto">
                    <a href="../index.html" class="text-decoration-none d-flex align-items-center gap-2" style="color: #F5EFE6;">
                        <img src="../img/icon_img.png" alt="Rutas Arequipa" width="32" height="32">
                        <h1 class="h5 fw-bold mb-0">Rutas Arequipa</h1>
                    </a>
                </div>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow-1 d-flex justify-content-center align-items-center py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
                    <!-- Registration Card -->
                    <div class="card shadow border-0 rounded-4" style="background-color: #4A5568;">
                        <div class="card-body p-4">
                            <!-- Header -->
                            <div class="text-center mb-4">
                                <h2 class="h4 fw-bold text-white mb-2">Crea tu cuenta</h2>
                                <p class="text-muted mb-0">Empieza a planificar tus viajes en autobús por Arequipa.</p>
                            </div>
                            
                            <!-- Error Messages -->
                            <% String error = request.getParameter("error"); %>
                            <% if (error != null) { %>
                                <div class="alert alert-danger text-center mb-3" role="alert">
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
                                <div class="alert alert-danger text-center mb-3" role="alert">
                                    <%= errorAttr %>
                                </div>
                            <% } %>

                            <!-- Registration Form -->
                            <form action="${pageContext.request.contextPath}/RegisterServlet" method="POST">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <input type="text" name="nombre" placeholder="Nombres" required 
                                               class="form-control form-control-lg border-1 rounded-3"
                                               style="background-color: #F5EFE6; color: #2D3748; border-color: #ccc;">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <input type="text" name="apellido" placeholder="Apellidos" required 
                                               class="form-control form-control-lg border-1 rounded-3"
                                               style="background-color: #F5EFE6; color: #2D3748; border-color: #ccc;">
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <input type="email" name="email" placeholder="Correo electrónico" required 
                                           class="form-control form-control-lg border-1 rounded-3"
                                           style="background-color: #F5EFE6; color: #2D3748; border-color: #ccc;">
                                </div>
                                
                                <div class="mb-3">
                                    <input type="password" name="password" placeholder="Contraseña" required 
                                           class="form-control form-control-lg border-1 rounded-3"
                                           style="background-color: #F5EFE6; color: #2D3748; border-color: #ccc;">
                                </div>
                                
                                <div class="mb-4">
                                    <input type="password" name="confirm-password" placeholder="Confirmar contraseña" required 
                                           class="form-control form-control-lg border-1 rounded-3"
                                           style="background-color: #F5EFE6; color: #2D3748; border-color: #ccc;">
                                </div>
                                
                                <button type="submit" class="btn btn-lg w-100 fw-bold rounded-3" 
                                        style="background-color: #DD6B20; color: #FFFFFF; border: none;">
                                    Registrarse
                                </button>
                            </form>

                            <!-- Footer Link -->
                            <p class="text-center mt-4 mb-0 text-muted">
                                ¿Ya tienes una cuenta?
                                <a href="login.jsp" class="text-decoration-none fw-bold" style="color: #DD6B20;">Inicia sesión aquí</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
