<%-- 
    Document   : favoritos
    Created on : 12 oct. 2025, 18:00:20
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="components/DsInicio.jsp" %>

        <!-- Main Content Area -->
        <div class="flex-grow-1 bg-light overflow-auto">
            <!-- Mobile Header -->
            <div class="d-lg-none bg-white shadow-sm p-3 border-bottom">
                <h2 class="mb-0 fw-bold text-dark">Favoritos</h2>
            </div>
            
            <!-- Desktop and Mobile Content -->
            <div class="container-fluid p-4">
                <!-- Page Title (Desktop only) -->
                <div class="d-none d-lg-block mb-4">
                    <h1 class="fw-bold text-dark mb-0">Favoritos</h1>
                </div>
                
                <!-- Favorites Content -->
                <div class="row g-4">
                    <!-- Rutas Favoritas Section -->
                    <div class="col-12 col-lg-6">
                        <div class="bg-white rounded-3 shadow-sm p-4 h-100">
                            <h3 class="fw-bold text-dark mb-4">Rutas Favoritas</h3>
                            
                            <!-- Ruta 101 -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Ruta 101</h5>
                                            <p class="mb-0 text-muted small">Centro - Cayma</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-heart-fill"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Ruta 203 -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Ruta 203</h5>
                                            <p class="mb-0 text-muted small">Miraflores - Yanahuara</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-heart-fill"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Ruta 305 -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Ruta 305</h5>
                                            <p class="mb-0 text-muted small">Sachaca - Cercado</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-heart-fill"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Lugares Favoritos Section -->
                    <div class="col-12 col-lg-6">
                        <div class="bg-white rounded-3 shadow-sm p-4 h-100">
                            <h3 class="fw-bold text-dark mb-4">Lugares Favoritos</h3>
                            
                            <!-- Casa -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-primary rounded-2 p-2 me-3">
                                            <i class="bi bi-house-fill text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Casa</h5>
                                            <p class="mb-0 text-muted small">Av. Ejercito 123, Yanahuara</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-warning btn-sm">
                                        <i class="bi bi-star-fill"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Trabajo -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-info rounded-2 p-2 me-3">
                                            <i class="bi bi-briefcase-fill text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Trabajo</h5>
                                            <p class="mb-0 text-muted small">Calle Mercaderes 456, Cercado</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-warning btn-sm">
                                        <i class="bi bi-star-fill"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Gimnasio -->
                            <div class="border rounded-3 p-3 mb-3 bg-light">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-success rounded-2 p-2 me-3">
                                            <i class="bi bi-lightning-fill text-white fs-5"></i>
                                        </div>
                                        <div>
                                            <h5 class="mb-1 fw-bold text-dark">Gimnasio</h5>
                                            <p class="mb-0 text-muted small">Av. Dolores 789, Jose Luis Bustamante</p>
                                        </div>
                                    </div>
                                    <button class="btn btn-outline-warning btn-sm">
                                        <i class="bi bi-star-fill"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Actions -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="bg-white rounded-3 shadow-sm p-4">
                            <h4 class="fw-bold text-dark mb-3">Acciones Rápidas</h4>
                            <div class="row g-3">
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-primary w-100 py-3 rounded-3">
                                        <i class="bi bi-plus-circle fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Agregar Ruta</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-success w-100 py-3 rounded-3">
                                        <i class="bi bi-geo-alt fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Agregar Lugar</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-info w-100 py-3 rounded-3">
                                        <i class="bi bi-map fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Ver en Mapa</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-warning w-100 py-3 rounded-3">
                                        <i class="bi bi-share fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Compartir</small>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

<%@ include file="components/DsFin.jsp" %>