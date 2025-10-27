<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Incluir layout según estado de sesión: invitados no deben ser redirigidos --%>
<%
    Object uid = session.getAttribute("userId");
    if (uid == null) {
%>
    <jsp:include page="components/DsInicioGuest.jsp" />
<%
    } else {
%>
    <jsp:include page="components/DsInicio.jsp" />
<%
    }
%>
        
        <!-- Main Content -->
        <div class="flex-grow-1 d-flex flex-column">
            <!-- Mobile Header with Menu Button -->
            <div class="bg-white d-lg-none p-3 shadow-sm">
                <div class="d-flex align-items-center">
                    <button class="btn btn-outline-dark me-3" type="button" data-bs-toggle="offcanvas" data-bs-target="#mobileSidebar">
                        <i class="bi bi-list"></i>
                    </button>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-geo-alt-fill text-warning me-2"></i>
                        <h5 class="mb-0 fw-bold">Enroute AQP</h5>
                    </div>
                </div>
            </div>

            <!-- Search Bar -->
            <div class="bg-white shadow-sm p-3 p-lg-4">
                <div class="row g-2 g-lg-3">
                    <div class="col-12 col-lg-5">
                        <div class="position-relative">
                            <input type="text" class="form-control form-control-lg ps-5" 
                                   placeholder="Lugar de inicio" id="origen">
                            <i class="bi bi-geo-alt position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
                        </div>
                    </div>
                    <div class="col-12 col-lg-5">
                        <div class="position-relative">
                            <input type="text" class="form-control form-control-lg ps-5" 
                                   placeholder="Lugar de destino" id="destino">
                            <i class="bi bi-geo-alt-fill position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
                        </div>
                    </div>
                    <div class="col-12 col-lg-2">
                        <button id="btn-buscar" type="button" class="btn btn-lg w-100 fw-bold" style="background-color: #DD6B20; color: white;" onclick="buscarRuta()">
                            <i class="bi bi-search me-2"></i>
                            <span class="d-none d-sm-inline">Buscar</span>
                            <span class="d-sm-none">🔍</span>
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Map Container -->
            <div class="flex-grow-1 position-relative">
                <div id="map" style="height: 100%; width: 100%; min-height: 400px;"></div>
            </div>
        </div>

<%@include file="components/DsFin.jsp"  %>