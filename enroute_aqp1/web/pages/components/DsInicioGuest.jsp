<%-- 
    Guest version of DsInicio without auth redirect.
    Provides the same layout but does not require a session.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    // Obtener información de sesión si existe (sin forzar login)
    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");
    Boolean isAdminObj = (Boolean) session.getAttribute("isAdmin");
    boolean isAdmin = isAdminObj != null && isAdminObj.booleanValue();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enroute AQP - Invitado</title>
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
<body style="font-family: 'Plus Jakarta Sans', sans-serif; height: 100vh; overflow-y: auto;">

    <div class="d-flex h-100">
        <!-- Mobile Menu Toggle Button -->
        <button class="d-lg-none position-fixed top-0 start-0 m-3" 
                type="button" data-bs-toggle="offcanvas" data-bs-target="#mobileSidebar" 
                style="z-index: 1050; background: #343a40; color: white; border: none; box-shadow: none; padding: 8px 12px; border-radius: 6px;">
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
            
            <!-- Navigation Menu (limited for guest) -->
            <nav class="py-3">
                <ul class="nav nav-pills flex-column">
                    <li class="nav-item mb-2">
                        <a href="mapa.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-geo-alt me-3"></i>
                            <span>Mapa</span>
                        </a>
                    </li>
                    <li class="nav-item mb-2">
                        <a href="login.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-box-arrow-in-right me-3"></i>
                            <span>Iniciar sesión</span>
                        </a>
                    </li>
                    <li class="nav-item mb-2">
                        <a href="register.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-person-plus me-3"></i>
                            <span>Registrarse</span>
                        </a>
                    </li>
                </ul>
            </nav>
            
            <!-- Guest Info -->
            <div class="p-4 border-top border-secondary">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle overflow-hidden me-3" style="width: 50px; height: 50px;">
                        <img src="https://avatars.githubusercontent.com/u/0?v=4" alt="Guest Avatar" 
                             class="w-100 h-100 object-fit-cover">
                    </div>
                    <div class="flex-grow-1">
                        <h6 class="mb-0 text-white"><c:out value="${sessionScope.userName != null ? sessionScope.userName : 'Invitado'}"/></h6>
                        <small class="text-white"><c:out value="${sessionScope.userEmail != null ? sessionScope.userEmail : 'Sin sesión'}"/></small>
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
                        <li class="nav-item mb-2">
                            <a href="mapa.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-geo-alt me-3"></i>
                                <span>Mapa</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="login.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-box-arrow-in-right me-3"></i>
                                <span>Iniciar sesión</span>
                            </a>
                        </li>
                        <li class="nav-item mb-2">
                            <a href="register.jsp" class="nav-link text-white d-flex align-items-center px-4 py-3">
                                <i class="bi bi-person-plus me-3"></i>
                                <span>Registrarse</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>