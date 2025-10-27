<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file = "components/DsInicioGuest.jsp" %>

        <!-- Main Content -->
        <div class="flex-grow-1 d-flex flex-column">
            <!-- Mobile Header with Menu Button -->
            <div class="bg-white d-lg-none p-3 shadow-sm">
                <div class="d-flex align-items-center">
                    <button class="btn btn-outline-dark me-3" type="button" data-bs-toggle="offcanvas" data-bs-target="#mobileSidebar">
                        <i class="bi bi-list"></i>
                    </button>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-speedometer2 text-primary me-2"></i>
                        <h5 class="mb-0 fw-bold">Modo Invitado</h5>
                    </div>
                </div>
            </div>

            <!-- Dashboard Content -->
            <div class="dashboard-container p-3 p-lg-4">
                <div class="row mb-4">
                    <div class="col-12">
                        <h2 class="fw-bold text-dark mb-1">Bienvenido a Enroute AQP</h2>
                        <p class="text-muted">Estás explorando como invitado. <a href="register.jsp" class="text-decoration-none fw-bold">Regístrate</a> o <a href="login.jsp" class="text-decoration-none fw-bold">inicia sesión</a> para acceder a todas las funciones.</p>
                    </div>
                </div>

                <!-- Alert Info -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="alert alert-info d-flex align-items-center" role="alert">
                            <i class="bi bi-info-circle-fill me-3 fs-4"></i>
                            <div>
                                <strong>Modo de prueba limitado</strong><br>
                                Solo puedes acceder al mapa. Regístrate para desbloquear todas las funcionalidades.
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Dashboard Cards con Bootstrap Grid -->
                <div class="row g-4">
                    <!-- Mapa - HABILITADO -->
                    <div class="col-lg-3 col-md-6">
                        <div class="card dashboard-card h-100 shadow">
                            <div class="card-body text-center p-4">
                                <div class="icon-circle bg-success">
                                    <i class="bi bi-geo-alt text-white"></i>
                                </div>
                                <h3 class="card-title">Mapa Interactivo</h3>
                                <p class="card-text">Explora el mapa de rutas de Arequipa</p>
                                <a href="mapa.jsp" class="btn btn-success">Ver Mapa</a>
                                <div class="mt-2">
                                    <span class="badge bg-success">Disponible</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Rutas - DESHABILITADO -->
                    <div class="col-lg-3 col-md-6">
                        <div class="card dashboard-card h-100 bg-light" style="opacity: 0.6;">
                            <div class="card-body text-center p-4">
                                <div class="icon-circle bg-secondary">
                                    <i class="bi bi-map text-white"></i>
                                </div>
                                <h3 class="card-title text-muted">Rutas Disponibles</h3>
                                <p class="card-text text-muted">Explora las rutas de Arequipa</p>
                                <button class="btn btn-secondary" disabled>
                                    <i class="bi bi-lock-fill me-1"></i> Bloqueado
                                </button>
                                <div class="mt-2">
                                    <span class="badge bg-secondary">Requiere registro</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Perfil - DESHABILITADO -->
                    <div class="col-lg-3 col-md-6">
                        <div class="card dashboard-card h-100 bg-light" style="opacity: 0.6;">
                            <div class="card-body text-center p-4">
                                <div class="icon-circle bg-secondary">
                                    <i class="bi bi-person text-white"></i>
                                </div>
                                <h3 class="card-title text-muted">Mi Perfil</h3>
                                <p class="card-text text-muted">Gestiona tu información personal</p>
                                <button class="btn btn-secondary" disabled>
                                    <i class="bi bi-lock-fill me-1"></i> Bloqueado
                                </button>
                                <div class="mt-2">
                                    <span class="badge bg-secondary">Requiere registro</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Favoritos - DESHABILITADO -->
                    <div class="col-lg-3 col-md-6">
                        <div class="card dashboard-card h-100 bg-light" style="opacity: 0.6;">
                            <div class="card-body text-center p-4">
                                <div class="icon-circle bg-secondary">
                                    <i class="bi bi-heart text-white"></i>
                                </div>
                                <h3 class="card-title text-muted">Favoritos</h3>
                                <p class="card-text text-muted">Guarda tus rutas favoritas</p>
                                <button class="btn btn-secondary" disabled>
                                    <i class="bi bi-lock-fill me-1"></i> Bloqueado
                                </button>
                                <div class="mt-2">
                                    <span class="badge bg-secondary">Requiere registro</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Historial - DESHABILITADO -->
                    <div class="col-lg-3 col-md-6">
                        <div class="card dashboard-card h-100 bg-light" style="opacity: 0.6;">
                            <div class="card-body text-center p-4">
                                <div class="icon-circle bg-secondary">
                                    <i class="bi bi-clock-history text-white"></i>
                                </div>
                                <h3 class="card-title text-muted">Historial</h3>
                                <p class="card-text text-muted">Revisa tu historial de viajes</p>
                                <button class="btn btn-secondary" disabled>
                                    <i class="bi bi-lock-fill me-1"></i> Bloqueado
                                </button>
                                <div class="mt-2">
                                    <span class="badge bg-secondary">Requiere registro</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Call to Action -->
                <div class="row mt-5">
                    <div class="col-12">
                        <div class="card border-0 shadow-lg" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <div class="card-body text-center p-5 text-white">
                                <h2 class="fw-bold mb-3">¿Listo para más?</h2>
                                <p class="fs-5 mb-4">Crea una cuenta gratis y desbloquea todas las funcionalidades de Enroute AQP</p>
                                <div class="d-flex gap-3 justify-content-center flex-wrap">
                                    <a href="register.jsp" class="btn btn-warning btn-lg px-5 py-3 rounded-pill fw-bold">
                                        <i class="bi bi-person-plus-fill me-2"></i> Registrarse Gratis
                                    </a>
                                    <a href="login.jsp" class="btn btn-outline-light btn-lg px-5 py-3 rounded-pill fw-bold">
                                        <i class="bi bi-box-arrow-in-right me-2"></i> Iniciar Sesión
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <style>
        .dashboard-container {
            max-width: 1400px;
            margin: 0 auto;
        }
        
        .dashboard-card {
            background: white;
            border-radius: 15px;
            transition: transform 0.3s, box-shadow 0.3s;
            border: 1px solid #e0e0e0;
        }
        
        .dashboard-card:not(.bg-light):hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }
        
        .icon-circle {
            width: 80px;
            height: 80px;
            background-color: #DD6B20;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            font-size: 2rem;
            color: white;
            transition: transform 0.3s;
        }
        
        .dashboard-card:not(.bg-light):hover .icon-circle {
            transform: scale(1.1);
        }
        
        .card-title {
            font-size: 1.3rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
            color: #333;
        }
        
        .card-text {
            color: #666;
            margin-bottom: 1rem;
        }
        
        .btn-custom {
            background-color: #DD6B20;
            color: white;
            padding: 0.75rem 2rem;
            border-radius: 25px;
            font-weight: bold;
            border: none;
            transition: all 0.3s;
        }
        
        .btn-custom:hover {
            background-color: #C55A15;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(221, 107, 32, 0.3);
        }
        </style>

<%@include file = "components/DsFin.jsp" %>
