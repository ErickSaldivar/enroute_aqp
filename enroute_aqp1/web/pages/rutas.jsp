<%-- 
    Document   : rutas
    Created on : 12 oct. 2025, 19:31:42
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="components/DsInicio.jsp" %>

        <!-- Main Content Area -->
        <div class="flex-grow-1 bg-light overflow-auto">
            <!-- Mobile Header -->
            <div class="d-lg-none bg-white shadow-sm p-3 border-bottom">
                <h2 class="mb-0 fw-bold text-dark">Rutas Disponibles</h2>
            </div>
            
            <!-- Desktop and Mobile Content -->
            <div class="container-fluid p-0 h-100">
                <div class="row g-0 h-100">
                    <!-- Left Panel - Search and Routes -->
                    <div class="col-12 col-lg-5 bg-white border-end">
                        <div class="p-4 h-100 d-flex flex-column">
                            <!-- Page Title (Desktop only) -->
                            <div class="d-none d-lg-block mb-4">
                                <h1 class="fw-bold text-dark mb-0">Rutas Disponibles</h1>
                            </div>
                            
                            <!-- Search Box -->
                            <div class="mb-4">
                                <div class="position-relative">
                                    <input type="text" class="form-control form-control-lg bg-light border-0 ps-5" 
                                           placeholder="Ingresa tu origen y destino" id="routeSearch">
                                    <i class="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted fs-5"></i>
                                </div>
                            </div>
                            
                            <!-- Available Routes -->
                            <div class="flex-grow-1">
                                <h4 class="fw-bold text-dark mb-3">Rutas Encontradas</h4>
                                
                                <!-- Route C-13 -->
                                <div class="border rounded-3 p-3 mb-3 bg-light route-card" style="cursor: pointer;" 
                                     data-route="C-13" onclick="selectRoute('C-13')">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h5 class="mb-1 fw-bold text-warning">Ruta C-13</h5>
                                            <p class="mb-0 text-muted small">Paucarpata - Centro</p>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-success">Activa</span>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Route A-38 -->
                                <div class="border rounded-3 p-3 mb-3 bg-light route-card" style="cursor: pointer;" 
                                     data-route="A-38" onclick="selectRoute('A-38')">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h5 class="mb-1 fw-bold text-warning">Ruta A-38</h5>
                                            <p class="mb-0 text-muted small">Cerro Colorado - Yanahuara</p>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-success">Activa</span>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Route T-2 -->
                                <div class="border rounded-3 p-3 mb-3 bg-light route-card" style="cursor: pointer;" 
                                     data-route="T-2" onclick="selectRoute('T-2')">
                                    <div class="d-flex align-items-center">
                                       <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h5 class="mb-1 fw-bold text-warning">Ruta T-2</h5>
                                            <p class="mb-0 text-muted small">Miraflores - Sachaca</p>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-success">Activa</span>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- More Routes -->
                                <div class="border rounded-3 p-3 mb-3 bg-light route-card" style="cursor: pointer;" 
                                     data-route="B-15" onclick="selectRoute('B-15')">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h5 class="mb-1 fw-bold text-warning">Ruta B-15</h5>
                                            <p class="mb-0 text-muted small">Alto Selva Alegre - Plaza de Armas</p>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-success">Activa</span>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="border rounded-3 p-3 mb-3 bg-light route-card" style="cursor: pointer;" 
                                     data-route="D-25" onclick="selectRoute('D-25')">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-2 p-2 me-3" style="background-color: #DD6B20;">
                                            <i class="bi bi-bus-front text-white fs-5"></i>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h5 class="mb-1 fw-bold text-warning">Ruta D-25</h5>
                                            <p class="mb-0 text-muted small">Characato - Mall Aventura</p>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-warning text-dark">Limitada</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Search Button -->
                            <div class="mt-4">
                                <button class="btn w-100 py-3 fw-bold text-white" style="background-color: #DD6B20;" onclick="searchRoutes()">
                                    <i class="bi bi-search me-2"></i>
                                    Buscar Ruta
                                </button>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Right Panel - Map -->
                    <div class="col-12 col-lg-7 position-relative">
                        <div class="h-100" id="routesMap" style="min-height: 500px; background: #f8f9fa;">
                            <!-- Map will be loaded here -->
                            <div class="d-flex align-items-center justify-content-center h-100">
                                <div class="text-center">
                                    <div class="spinner-border text-warning mb-3" role="status">
                                        <span class="visually-hidden">Cargando mapa...</span>
                                    </div>
                                    <p class="text-muted">Cargando mapa de rutas...</p>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Map Controls -->
                        <div class="position-absolute top-0 end-0 m-3">
                            <div class="btn-group-vertical shadow-sm">
                                <button class="btn btn-white border" onclick="zoomIn()">
                                    <i class="bi bi-plus-lg"></i>
                                </button>
                                <button class="btn btn-white border" onclick="zoomOut()">
                                    <i class="bi bi-dash-lg"></i>
                                </button>
                            </div>
                        </div>
                        
                        <!-- Map Legend -->
                        <div class="position-absolute bottom-0 start-0 m-3">
                            <div class="bg-white rounded-3 shadow-sm p-3" style="min-width: 200px;">
                                <h6 class="fw-bold mb-2">Leyenda</h6>
                                <div class="d-flex align-items-center mb-2">
                                    <div class="bg-success rounded-circle me-2" style="width: 12px; height: 12px;"></div>
                                    <small>Rutas activas</small>
                                </div>
                                <div class="d-flex align-items-center mb-2">
                                    <div class="bg-warning rounded-circle me-2" style="width: 12px; height: 12px;"></div>
                                    <small>Rutas limitadas</small>
                                </div>
                                <div class="d-flex align-items-center">
                                    <div class="bg-danger rounded-circle me-2" style="width: 12px; height: 12px;"></div>
                                    <small>Fuera de servicio</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Route selection functionality
            let selectedRoute = null;
            
            function selectRoute(routeId) {
                // Remove previous selection
                document.querySelectorAll('.route-card').forEach(card => {
                    card.classList.remove('border-warning', 'bg-warning-subtle');
                    card.classList.add('bg-light');
                });
                
                // Add selection to clicked route
                const selectedCard = document.querySelector(`[data-route="${routeId}"]`);
                if (selectedCard) {
                    selectedCard.classList.remove('bg-light');
                    selectedCard.classList.add('border-warning', 'bg-warning-subtle');
                }
                
                selectedRoute = routeId;
                console.log('Selected route:', routeId);
                
                // Here you would typically update the map to show the selected route
                showRouteOnMap(routeId);
            }
            
            function searchRoutes() {
                const searchTerm = document.getElementById('routeSearch').value;
                if (searchTerm.trim()) {
                    console.log('Searching for routes:', searchTerm);
                    // Here you would implement the actual search functionality
                    alert('Buscando rutas para: ' + searchTerm);
                } else {
                    alert('Por favor, ingresa un origen y destino');
                }
            }
            
            function showRouteOnMap(routeId) {
                // Placeholder for map integration
                console.log('Showing route on map:', routeId);
                
                // Update map loading message
                const mapElement = document.getElementById('routesMap');
                mapElement.innerHTML = `
                    <div class="d-flex align-items-center justify-content-center h-100">
                        <div class="text-center">
                            <i class="bi bi-map text-warning fs-1 mb-3"></i>
                            <h5 class="text-dark">Mostrando ${routeId}</h5>
                            <p class="text-muted">Visualizando ruta en el mapa...</p>
                        </div>
                    </div>
                `;
            }
            
            function zoomIn() {
                console.log('Zoom in');
                // Map zoom functionality would go here
            }
            
            function zoomOut() {
                console.log('Zoom out');
                // Map zoom functionality would go here
            }
            
            // Initialize map when page loads
            document.addEventListener('DOMContentLoaded', function() {
                // Initialize the map here
                console.log('Initializing routes map...');
                
                // Simulate map loading
                setTimeout(() => {
                    const mapElement = document.getElementById('routesMap');
                    mapElement.innerHTML = `
                        <div class="d-flex align-items-center justify-content-center h-100">
                            <div class="text-center">
                                <i class="bi bi-geo-alt text-warning fs-1 mb-3"></i>
                                <h5 class="text-dark">Mapa de Rutas Arequipa</h5>
                                <p class="text-muted">Selecciona una ruta para ver su recorrido</p>
                            </div>
                        </div>
                    `;
                }, 2000);
            });
            
            // Search functionality
            document.getElementById('routeSearch').addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    searchRoutes();
                }
            });
        </script>

<%@include file="components/DsFin.jsp" %>
