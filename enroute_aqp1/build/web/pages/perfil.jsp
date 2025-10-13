<%-- 
    Document   : perfil
    Created on : 12 oct. 2025, 18:39:49
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/DsInicio.jsp" %>

        <!-- Main Content Area -->
        <div class="flex-grow-1 bg-light overflow-auto">
            <!-- Mobile Header -->
            <div class="d-lg-none bg-white shadow-sm p-3 border-bottom">
                <h2 class="mb-0 fw-bold text-dark">Mi Perfil</h2>
            </div>
            
            <!-- Desktop and Mobile Content -->
            <div class="container-fluid p-4">
                <!-- Page Title (Desktop only) -->
                <div class="d-none d-lg-block mb-4">
                    <h1 class="fw-bold text-dark mb-0">Mi Perfil</h1>
                </div>
                
                <!-- Profile Content -->
                <div class="row g-4">
                    <!-- Personal Data Section -->
                    <div class="col-12">
                        <div class="bg-white rounded-3 shadow-sm p-4">
                            <h3 class="fw-bold mb-4" style="color: #DD6B20;">
                                <i class="bi bi-person-circle me-2"></i>
                                Datos Personales
                            </h3>
                            
                            <form>
                                <div class="row g-3">
                                    <!-- Name Field -->
                                    <div class="col-12">
                                        <label for="nombre" class="form-label fw-medium text-dark">Nombre</label>
                                        <input type="text" class="form-control form-control-lg bg-light border-0" 
                                               id="nombre" name="nombre" placeholder="Tu nombre" 
                                               value="${sessionScope.userName != null ? sessionScope.userName : ''}">
                                    </div>
                                    
                                    <!-- Email Field -->
                                    <div class="col-12">
                                        <label for="email" class="form-label fw-medium text-dark">Correo electrónico</label>
                                        <input type="email" class="form-control form-control-lg bg-light border-0" 
                                               id="email" name="email" placeholder="tu@correo.com" 
                                               value="${sessionScope.userEmail != null ? sessionScope.userEmail : ''}">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <!-- Route Preferences Section -->
                    <div class="col-12">
                        <div class="bg-white rounded-3 shadow-sm p-4">
                            <h4 class="fw-bold text-dark mb-4">Preferencias de Ruta</h4>
                            
                            <div class="row g-3">
                                <!-- Avoid Transfers -->
                                <div class="col-12">
                                    <div class="form-check d-flex align-items-center p-3 bg-light rounded-3">
                                        <input class="form-check-input me-3" type="checkbox" 
                                               id="evitarTransbordos" name="evitarTransbordos" checked 
                                               style="transform: scale(1.2);">
                                        <div>
                                            <label class="form-check-label fw-medium text-dark" for="evitarTransbordos">
                                                Evitar transbordos
                                            </label>
                                            <p class="mb-0 small text-muted">Priorizar rutas directas sin cambios de bus</p>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Fastest Routes -->
                                <div class="col-12">
                                    <div class="form-check d-flex align-items-center p-3 bg-light rounded-3">
                                        <input class="form-check-input me-3" type="checkbox" 
                                               id="rutasRapidas" name="rutasRapidas" 
                                               style="transform: scale(1.2);">
                                        <div>
                                            <label class="form-check-label fw-medium text-dark" for="rutasRapidas">
                                                Rutas más rápidas
                                            </label>
                                            <p class="mb-0 small text-muted">Mostrar primero las opciones de menor tiempo</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Notification Settings Section -->
                    <div class="col-12">
                        <div class="bg-white rounded-3 shadow-sm p-4">
                            <h4 class="fw-bold text-dark mb-4">Configuración de Notificaciones</h4>
                            
                            <div class="row g-3">
                                <!-- Route Changes Notifications -->
                                <div class="col-12">
                                    <div class="form-check d-flex align-items-center p-3 bg-light rounded-3">
                                        <input class="form-check-input me-3" type="checkbox" 
                                               id="notificacionesCambios" name="notificacionesCambios" checked 
                                               style="transform: scale(1.2);">
                                        <div>
                                            <label class="form-check-label fw-medium text-dark" for="notificacionesCambios">
                                                Notificaciones de cambios en rutas
                                            </label>
                                            <p class="mb-0 small text-muted">Recibir alertas sobre modificaciones en tus rutas favoritas</p>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Service Updates -->
                                <div class="col-12">
                                    <div class="form-check d-flex align-items-center p-3 bg-light rounded-3">
                                        <input class="form-check-input me-3" type="checkbox" 
                                               id="actualizacionesServicio" name="actualizacionesServicio" 
                                               style="transform: scale(1.2);">
                                        <div>
                                            <label class="form-check-label fw-medium text-dark" for="actualizacionesServicio">
                                                Actualizaciones del servicio
                                            </label>
                                            <p class="mb-0 small text-muted">Información sobre mejoras y nuevas funcionalidades</p>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Traffic Alerts -->
                                <div class="col-12">
                                    <div class="form-check d-flex align-items-center p-3 bg-light rounded-3">
                                        <input class="form-check-input me-3" type="checkbox" 
                                               id="alertasTrafico" name="alertasTrafico"
                                               style="transform: scale(1.2);">
                                        <div>
                                            <label class="form-check-label fw-medium text-dark" for="alertasTrafico">
                                                Alertas de tráfico
                                            </label>
                                            <p class="mb-0 small text-muted">Notificaciones sobre congestión y retrasos</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Additional Profile Options -->
                    <div class="col-12">
                        <div class="bg-white rounded-3 shadow-sm p-4">
                            <h4 class="fw-bold text-dark mb-4">Otras Opciones</h4>
                            
                            <div class="row g-3">
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-primary w-100 py-3 rounded-3">
                                        <i class="bi bi-shield-lock fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Cambiar Contraseña</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-info w-100 py-3 rounded-3">
                                        <i class="bi bi-download fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Exportar Datos</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-secondary w-100 py-3 rounded-3">
                                        <i class="bi bi-question-circle fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Ayuda</small>
                                    </button>
                                </div>
                                <div class="col-6 col-md-3">
                                    <button class="btn btn-outline-danger w-100 py-3 rounded-3">
                                        <i class="bi bi-trash fs-4 d-block mb-2"></i>
                                        <small class="fw-medium">Eliminar Cuenta</small>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Save Button -->
                    <div class="col-12">
                        <div class="d-grid">
                            <button type="submit" class="btn btn-warning btn-lg py-3 fw-bold text-white rounded-3" style="background-color: #DD6B20;">
                                <i class="bi bi-check-circle me-2"></i>
                                Guardar Cambios
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

<%@include file="components/DsFin.jsp" %>