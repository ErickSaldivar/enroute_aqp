<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

<%
    // Verificar si el usuario está logueado
    String userEmail = (String) session.getAttribute("userEmail");
    if (userEmail == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Enroute AQP</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;700;800&display=swap" rel="stylesheet">
</head>
<body class="bg-dark text-light" style="font-family: 'Plus Jakarta Sans', sans-serif;">

    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg bg-dark bg-opacity-75 border-bottom border-secondary sticky-top">
        <div class="container">
            <!-- Logo -->
            <a class="navbar-brand text-light d-flex align-items-center fw-bold fs-5" href="../index.html">
                <img src="../img/icon_img.png" alt="Logo" style="width: 32px; height: 32px;" class="me-2">
                <span>Enroute AQP</span>
            </a>
            
            <!-- User Info -->
            <div class="d-flex align-items-center gap-3">
                <span class="text-light fw-medium">¡Bienvenido ${sessionScope.userName}!</span>
                <div class="rounded-circle overflow-hidden" style="width: 40px; height: 40px;">
                    <img src="https://randomuser.me/api/portraits/men/44.jpg" alt="Avatar" 
                         class="w-100 h-100 object-fit-cover">
                </div>
            </div>
        </div>
    </nav>

    <!-- Dashboard Content -->
    <div class="container py-5">
        <!-- Dashboard Grid -->
        <div class="row g-4 mt-3">
            <!-- Rutas Disponibles -->
            <div class="col-lg-6 col-xl-3">
                <div class="card bg-secondary border-0 shadow h-100 text-center" style="background-color: #2c2c2c !important;">
                    <div class="card-body p-4">
                        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3 shadow-sm" 
                             style="width: 60px; height: 60px;">
                            <svg viewBox="0 0 24 24" style="width: 32px; height: 32px; fill: black;">
                                <path d="M20.5 3l-5.5 2.5-6-2.5-5.5 2.5v16l6-2.5 6 2.5 5.5-2.5v-16zm-6.5 17.11l-6-2.5v-13.61l6 2.5v13.61zm7-2.5l-5 2.27v-13.61l5-2.27v13.61z"/>
                            </svg>
                        </div>
                        <h3 class="h5 text-light fw-bold mb-3">Rutas Disponibles</h3>
                        <p class="text-muted mb-4">Explora las rutas turísticas más populares de Arequipa</p>
                        <button class="btn btn-warning fw-bold rounded-pill px-4">Ver Rutas</button>
                    </div>
                </div>
            </div>
            
            <!-- Mi Perfil -->
            <div class="col-lg-6 col-xl-3">
                <div class="card bg-secondary border-0 shadow h-100 text-center" style="background-color: #2c2c2c !important;">
                    <div class="card-body p-4">
                        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3 shadow-sm" 
                             style="width: 60px; height: 60px;">
                            <svg viewBox="0 0 24 24" style="width: 32px; height: 32px; fill: black;">
                                <path d="M12 12c2.7 0 5-2.3 5-5s-2.3-5-5-5-5 2.3-5 5 2.3 5 5 5zm0 2c-3.3 0-10 1.7-10 5v3h20v-3c0-3.3-6.7-5-10-5z"/>
                            </svg>
                        </div>
                        <h3 class="h5 text-light fw-bold mb-3">Mi Perfil</h3>
                        <p class="text-muted mb-4">Gestiona tu información personal y preferencias</p>
                        <button class="btn btn-warning fw-bold rounded-pill px-4">Editar Perfil</button>
                    </div>
                </div>
            </div>
            
            <!-- Favoritos -->
            <div class="col-lg-6 col-xl-3">
                <div class="card bg-secondary border-0 shadow h-100 text-center" style="background-color: #2c2c2c !important;">
                    <div class="card-body p-4">
                        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3 shadow-sm" 
                             style="width: 60px; height: 60px;">
                            <svg viewBox="0 0 24 24" style="width: 32px; height: 32px; fill: black;">
                                <path d="M12 21.35l-1.45-1.32c-5.05-4.6-8.35-7.6-8.35-11.07 0-2.54 2.07-4.61 4.61-4.61 1.74 0 3.41 1.01 4.19 2.61.78-1.6 2.45-2.61 4.19-2.61 2.54 0 4.61 2.07 4.61 4.61 0 3.47-3.3 6.47-8.35 11.07z"/>
                            </svg>
                        </div>
                        <h3 class="h5 text-light fw-bold mb-3">Favoritos</h3>
                        <p class="text-muted mb-4">Revisa tus rutas y lugares favoritos guardados</p>
                        <button class="btn btn-warning fw-bold rounded-pill px-4">Ver Favoritos</button>
                    </div>
                </div>
            </div>
            
            <!-- Historial -->
            <div class="col-lg-6 col-xl-3">
                <div class="card bg-secondary border-0 shadow h-100 text-center" style="background-color: #2c2c2c !important;">
                    <div class="card-body p-4">
                        <div class="bg-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3 shadow-sm" 
                             style="width: 60px; height: 60px;">
                            <svg viewBox="0 0 24 24" style="width: 32px; height: 32px; fill: black;">
                                <path d="M12 8v5l4.28 2.54.72-1.21-3.5-2.08V8zm0-6c-5.52 0-10 4.48-10 10s4.48 10 10 10 10-4.48 10-10-4.48-10-10-10zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/>
                            </svg>
                        </div>
                        <h3 class="h5 text-light fw-bold mb-3">Historial</h3>
                        <p class="text-muted mb-4">Consulta tu historial de rutas visitadas</p>
                        <button class="btn btn-warning fw-bold rounded-pill px-4">Ver Historial</button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Logout Button -->
        <div class="text-center mt-5">
            <a href="../LogoutServlet" class="btn btn-danger px-4 py-2 fw-bold rounded-pill">
                Cerrar Sesión
            </a>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
