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
        <!-- Sidebar -->
        <div class="bg-dark text-white d-flex flex-column" style="width: 280px; min-height: 100vh;">
            <!-- Logo -->
            <div class="p-4 border-bottom border-secondary">
                <div class="d-flex align-items-center">
                    <i class="bi bi-geo-alt-fill text-warning me-2 fs-4"></i>
                    <h4 class="mb-0 fw-bold text-white">Rutas Arequipa</h4>
                </div>
            </div>
            
            <!-- Navigation Menu -->
            <nav class="flex-grow-1 py-3">
                <ul class="nav nav-pills flex-column">
                    <li class="nav-item mb-2">
                        <a href="#" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-person me-3"></i>
                            <span>Mi Perfil</span>
                        </a>
                    </li>
                    <li class="nav-item mb-2">
                        <a href="#" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-heart me-3"></i>
                            <span>Favoritos</span>
                        </a>
                    </li>
                    <li class="nav-item mb-2">
                        <a href="#" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-clock-history me-3"></i>
                            <span>Historial</span>
                        </a>
                    </li>
                    <li class="nav-item mb-2">
                        <a href="#" class="nav-link text-white d-flex align-items-center px-4 py-3">
                            <i class="bi bi-map me-3"></i>
                            <span>Rutas Disponibles</span>
                        </a>
                    </li>
                </ul>
            </nav>
            
            <!-- User Profile -->
            <div class="p-4 border-top border-secondary">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle overflow-hidden me-3" style="width: 50px; height: 50px;">
                        <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="Sofia" 
                             class="w-100 h-100 object-fit-cover">
                    </div>
                    <div class="flex-grow-1">
                        <h6 class="mb-0 text-white">Sofia</h6>
                        <small class="text-muted">${sessionScope.userEmail}</small>
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

        <!-- Main Content -->
        <div class="flex-grow-1 d-flex flex-column">
            <!-- Search Bar -->
            <div class="bg-white shadow-sm p-4">
                <div class="row g-3">
                    <div class="col-md-5">
                        <div class="position-relative">
                            <input type="text" class="form-control form-control-lg ps-5" 
                                   placeholder="Lugar de inicio" id="origen">
                            <i class="bi bi-geo-alt position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
                        </div>
                    </div>
                    <div class="col-md-5">
                        <div class="position-relative">
                            <input type="text" class="form-control form-control-lg ps-5" 
                                   placeholder="Lugar de destino" id="destino">
                            <i class="bi bi-geo-alt-fill position-absolute top-50 start-0 translate-middle-y ms-3 text-muted"></i>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button class="btn btn-warning btn-lg w-100 fw-bold" onclick="buscarRuta()">
                            <i class="bi bi-search me-2"></i>
                            Buscar
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Map Container -->
            <div class="flex-grow-1 position-relative">
                <div id="map" style="height: 100%; width: 100%;"></div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Leaflet JS -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    
    <script>
        // Inicializar el mapa
        let map;
        let markers = [];
        
        function initMap() {
            // Centrar el mapa en Arequipa, Perú
            map = L.map('map').setView([-16.4090, -71.5375], 12);
            
            // Agregar capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            
            // Agregar marcador en el centro de Arequipa
            L.marker([-16.4090, -71.5375]).addTo(map)
                .bindPopup('Arequipa - Ciudad Blanca')
                .openPopup();
        }
        
        function buscarRuta() {
            const origen = document.getElementById('origen').value;
            const destino = document.getElementById('destino').value;
            
            if (origen && destino) {
                alert('Buscando ruta de: ' + origen + ' a: ' + destino);
                // Aquí se implementaría la lógica de búsqueda de rutas
            } else {
                alert('Por favor, completa ambos campos');
            }
        }
        
        // Inicializar el mapa cuando cargue la página
        document.addEventListener('DOMContentLoaded', function() {
            initMap();
        });
        
        // Hacer que el mapa se redimensione correctamente
        window.addEventListener('resize', function() {
            if (map) {
                map.invalidateSize();
            }
        });
    </script>
</body>
</html>
