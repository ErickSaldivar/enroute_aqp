<%-- 
    Document   : DsInicio
    Created on : 12 oct. 2025, 12:12:45
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    // Verificar si el usuario está logueado
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    
    // Obtener información del usuario
    String userName = (String) session.getAttribute("userName");
    String userLastName = (String) session.getAttribute("userLastName");
    String userEmail = (String) session.getAttribute("userEmail");
    Boolean isAdminObj = (Boolean) session.getAttribute("isAdmin");
    
    // Debug: Imprimir el valor booleano del admin
    System.out.println("DEBUG DsInicio.jsp - Email: " + userEmail + ", Es Admin: " + isAdminObj);
    
    // Verificación doble para administradores
    boolean isAdmin = false;
    
    // Estrategia 1: Verificar por email específico (alternativa robusta)
    if (userEmail != null) {
        String emailLower = userEmail.toLowerCase();
        if ("jordi@admin.com".equals(emailLower) || "admin@enroute.com".equals(emailLower)) {
            isAdmin = true;
            System.out.println("DEBUG DsInicio.jsp - Usuario admin detectado por email: " + emailLower);
        }
    }
    
    // Estrategia 2: Verificar por valor de sesión si no se detectó por email
    if (!isAdmin && isAdminObj != null) {
        isAdmin = isAdminObj.booleanValue();
        System.out.println("DEBUG DsInicio.jsp - Usuario admin detectado por sesión: " + isAdmin);
    }
    
    // Debug: Imprimir el resultado de la validación doble
    System.out.println("DEBUG DsInicio.jsp - isAdmin final (verificación doble): " + isAdmin);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Enroute AQP</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;700;800&display=swap" rel="stylesheet">
</head>
<body style="font-family: 'Plus Jakarta Sans', sans-serif; height: 100vh; overflow: hidden;">

    <div class="d-flex h-100">
        <!-- Mobile Menu Toggle Button -->
        <button class="btn btn-dark d-lg-none position-fixed top-0 start-0 m-3 z-3" 
                type="button" data-bs-toggle="offcanvas" data-bs-target="#mobileSidebar" 
                style="z-index: 1050;">
            <i class="bi bi-list fs-4"></i>
        </button>

        <!-- Sidebar for Desktop -->
        <div class="bg-dark text-white d-none d-lg-flex flex-column" style="width: 280px; min-height: 100vh;">
            <!-- Logo -->
            <div class="p-4 border-bottom border-secondary">
                <div class="d-flex align-items-center">
                    <img src="${pageContext.request.contextPath}/img/icon_img.png" alt="Logo" style="height: 40px" class="me-2">
                    <h4 class="mb-0 fw-bold text-white">Enroute AQP</h4>
                </div>
            </div>
            
            <!-- Navigation Menu -->
            <nav class="py-3">
                    <ul class="nav nav-pills flex-column">
                        <!-- Menú común para todos los usuarios -->
                        <% if (isAdmin) { %>
                        <!-- Opción especial para Administradores: Dashboard -->
                        <li class="nav-item mb-2">
                            <a href="dashboard.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-speedometer2 me-3"></i>
                                <span>Dashboard</span>
                            </a>
                        </li>
                        <% } %>
                        <!-- Opciones comunes para todos -->
                        <li class="nav-item mb-2">
                            <a href="mapa.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-bus-front me-3"></i>
                                <span>Viajar</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="favoritos.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-heart me-3"></i>
                                <span>Favoritos</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="historial.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-clock-history me-3"></i>
                                <span>Historial</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="rutas.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-map me-3"></i>
                                <span>Rutas Disponibles</span>
                            </a>
                        </li>
                        <!-- Perfil disponible para todos -->
                        <li class="nav-item mb-2">
                            <a href="perfil.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-person me-3"></i>
                                <span>Mi Perfil</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            
            <!-- User Profile -->
            <div class="p-4 border-top border-secondary">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle overflow-hidden me-3" style="width: 50px; height: 50px;">
                        <img src="https://randomuser.me/api/portraits/men/44.jpg" alt="User Avatar" 
                             class="w-100 h-100 object-fit-cover">
                    </div>
                    <div class="flex-grow-1">
                        <h6 class="mb-0 text-white">${sessionScope.userName}</h6>
                        <small class="text-white">${sessionScope.userEmail}</small>
                    </div>
                    <div class="dropdown">
                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                            <i class="bi bi-three-dots-vertical"></i>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="../LogoutServlet">Cerrar Sesión</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Mobile Sidebar (Offcanvas) -->
        <div class="offcanvas offcanvas-start bg-dark text-white d-lg-none" tabindex="-1" id="mobileSidebar">
            <div class="offcanvas-header border-bottom border-secondary">
                <div class="d-flex align-items-center">
                    <i class="bi bi-geo-alt-fill text-warning me-2 fs-4"></i>
                    <h4 class="mb-0 fw-bold text-white">Enroute AQP</h4>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"></button>
            </div>
            <div class="offcanvas-body p-0">
                <nav class="py-3">
                    <ul class="nav nav-pills flex-column">
                        <!-- Menú móvil común para todos los usuarios -->
                        <% if (isAdmin) { %>
                        <!-- Opción especial para Administradores: Dashboard -->
                        <li class="nav-item mb-2">
                            <a href="dashboard.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-speedometer2 me-3"></i>
                                <span>Dashboard</span>
                            </a>
                        </li>
                        <% } %>
                        <!-- Opciones móviles comunes para todos -->
                        <li class="nav-item mb-2">
                            <a href="favoritos.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3" onclick="window.location.href='favoritos.jsp'; return false;">
                                <i class="bi bi-heart me-3"></i>
                                <span>Favoritos</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="historial.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3" onclick="window.location.href='historial.jsp'; return false;">
                                <i class="bi bi-clock-history me-3"></i>
                                <span>Historial</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="rutas.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3" onclick="window.location.href='rutas.jsp'; return false;">
                                <i class="bi bi-map me-3"></i>
                                <span>Rutas Disponibles</span>
                            </a>
                        </li>
                        <!-- Perfil disponible para todos -->
                        <li class="nav-item mb-2">
                            <a href="perfil.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3" onclick="window.location.href='perfil.jsp'; return false;">
                                <i class="bi bi-person me-3"></i>
                                <span>Mi Perfil</span>
                            </a>
                        </li>
                    </ul>
                </nav>
                
                <!-- Mobile User Profile -->
                <div class="p-4 border-top border-secondary mt-auto">
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle overflow-hidden me-3" style="width: 50px; height: 50px;">
                            <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="User Avatar" 
                                 class="w-100 h-100 object-fit-cover">
                        </div>
                        <div class="flex-grow-1">
                            <h6 class="mb-0 text-white">${sessionScope.userName}</h6>
                            <small class="text-white">${sessionScope.userEmail}</small>
                        </div>
                    </div>
                    <div class="mt-3">
                        <a href="../LogoutServlet" class="btn btn-outline-light btn-sm w-100">Cerrar Sesión</a>
                    </div>
                </div>
            </div>
        </div>