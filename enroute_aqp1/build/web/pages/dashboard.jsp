<%-- 
    ========== DASHBOARD PRINCIPAL - PÁGINA JSP ==========
    
    Esta página JSP implementa el dashboard principal del sistema aplicando
    los conceptos y patrones vistos en clase:
    
    CONCEPTOS JSP IMPLEMENTADOS:
    - Directivas JSP: @page, @include para modularización
    - Inclusión de archivos JSP para reutilización de código
    - Integración con Bootstrap como framework frontend
    - Separación de responsabilidades (MVC)
    
    PATRONES APLICADOS:
    - MVC: Esta página actúa como VISTA del patrón MVC
    - Modularización: Uso de includes para separar lógica de protección
    - Reutilización: Componentes JSP reutilizables (DsInicio.jsp)
    
    FUNCIONALIDADES CORE DEL NEGOCIO:
    - Dashboard principal con KPIs del sistema
    - Visualización de métricas de usuarios y rutas
    - Gráficos interactivos con Chart.js
    - Control de acceso solo para administradores
    
    INTEGRACIÓN CON FRAMEWORKS FRONTEND:
    - Bootstrap 5 para diseño responsivo
    - Chart.js para visualización de datos
    - Bootstrap Icons para iconografía
    
    @author Equipo EnRoute AQP
    @version 2.0 - Segundo Avance del Proyecto Final
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- ========== INCLUSIÓN DE PROTECCIÓN DE ACCESO ========== --%>
<%-- Incluye la lógica de verificación de sesión y permisos de administrador --%>
<%@include file = "dashboard-protection.jsp" %>

<%-- ========== INCLUSIÓN DEL COMPONENTE PRINCIPAL ========== --%>
<%-- Incluye el componente principal del dashboard con la estructura HTML --%>
<%@include file = "components/DsInicio.jsp" %>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<!-- Custom Styles -->
<link rel="stylesheet" href="../css/custom-styles.css">

        <!-- Main Content -->
        <div class="flex-grow-1 d-flex flex-column">
            <!-- Mobile Header -->
            <div class="bg-white d-lg-none p-3 shadow-sm">
                <div class="d-flex align-items-center justify-content-center">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-speedometer2 text-primary-custom me-2"></i>
                        <h5 class="mb-0 fw-bold">Dashboard</h5>
                    </div>
                </div>
            </div>

            <!-- Dashboard Content -->
            <div class="container-fluid p-3 p-lg-4">

    <!-- Navbar con Bootstrap -->
    <nav class="navbar navbar-expand-lg navbar-custom mb-4">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="bi bi-geo-alt-fill me-2"></i>
                EnRoute AQP
            </a>
        </div>
    </nav>

    <!-- KPI Section con Bootstrap Grid -->
    <div class="row g-4 mb-4">
            <div class="col-lg-3 col-md-6">
                <div class="card kpi-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="kpi-number" id="totalUsers">1,247</div>
                        <div class="kpi-label">Usuarios Activos</div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card kpi-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="kpi-number" id="totalRoutes">45</div>
                        <div class="kpi-label">Rutas Disponibles</div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card kpi-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="kpi-number" id="totalTrips">8,932</div>
                        <div class="kpi-label">Viajes Realizados</div>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card kpi-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="kpi-number" id="satisfaction">94%</div>
                        <div class="kpi-label">Satisfacción</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts Section con Bootstrap Grid -->
        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="card chart-card h-100">
                    <div class="card-body p-4">
                        <h5 class="chart-title card-title mb-3">Usuarios por Mes</h5>
                        <div class="chart-container">
                            <canvas id="usersChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="card chart-card h-100">
                    <div class="card-body p-4">
                        <h5 class="chart-title card-title mb-3">Rutas Más Populares</h5>
                        <div class="chart-container">
                            <canvas id="routesChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Logout Button -->
        <div class="text-center mt-5">
            <a href="LogoutServlet" class="btn btn-logout btn-lg">
                <i class="bi bi-box-arrow-right me-2"></i>
                Cerrar Sesión
            </a>
        </div>
    </div>
        </div>
    </div>

    <!-- Chart.js Scripts -->
    <script>
        // Animación de números KPI
        function animateNumber(elementId, finalNumber, duration = 2000) {
            const element = document.getElementById(elementId);
            const startNumber = 0;
            const increment = finalNumber / (duration / 16);
            let currentNumber = startNumber;
            
            const timer = setInterval(() => {
                currentNumber += increment;
                if (currentNumber >= finalNumber) {
                    currentNumber = finalNumber;
                    clearInterval(timer);
                }
                
                if (elementId === 'satisfaction') {
                    element.textContent = Math.floor(currentNumber) + '%';
                } else {
                    element.textContent = Math.floor(currentNumber).toLocaleString();
                }
            }, 16);
        }

        // Gráfico de Usuarios por Mes
        const usersCtx = document.getElementById('usersChart').getContext('2d');
        const usersChart = new Chart(usersCtx, {
            type: 'line',
            data: {
                labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
                datasets: [{
                    label: 'Usuarios Registrados',
                    data: [65, 89, 120, 151, 189, 234, 298, 356, 412, 478, 523, 587],
                    borderColor: 'rgb(102, 126, 234)',
                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: 'rgb(102, 126, 234)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: 'rgba(0, 0, 0, 0.1)'
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                },
                elements: {
                    point: {
                        hoverRadius: 8
                    }
                }
            }
        });

        // Gráfico de Rutas Más Populares
        const routesCtx = document.getElementById('routesChart').getContext('2d');
        const routesChart = new Chart(routesCtx, {
            type: 'doughnut',
            data: {
                labels: ['Centro Histórico', 'Miraflores', 'Yanahuara', 'Cayma', 'Cerro Colorado'],
                datasets: [{
                    data: [35, 25, 20, 12, 8],
                    backgroundColor: [
                        'rgba(102, 126, 234, 0.8)',
                        'rgba(118, 75, 162, 0.8)',
                        'rgba(255, 107, 107, 0.8)',
                        'rgba(54, 162, 235, 0.8)',
                        'rgba(255, 206, 84, 0.8)'
                    ],
                    borderColor: [
                        'rgb(102, 126, 234)',
                        'rgb(118, 75, 162)',
                        'rgb(255, 107, 107)',
                        'rgb(54, 162, 235)',
                        'rgb(255, 206, 84)'
                    ],
                    borderWidth: 2,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true,
                            font: {
                                size: 12
                            }
                        }
                    }
                },
                cutout: '60%'
            }
        });

        // Inicializar animaciones cuando se carga la página
        window.addEventListener('load', () => {
            setTimeout(() => {
                animateNumber('totalUsers', 1247);
                animateNumber('totalRoutes', 45);
                animateNumber('totalTrips', 8932);
                animateNumber('satisfaction', 94);
            }, 500);
        });

        // Actualizar datos cada 30 segundos (simulación)
        setInterval(() => {
            // Simular actualización de KPIs
            const userIncrease = Math.floor(Math.random() * 5) + 1;
            const tripIncrease = Math.floor(Math.random() * 20) + 5;
            
            document.getElementById('totalUsers').textContent = (1247 + userIncrease).toLocaleString();
            document.getElementById('totalTrips').textContent = (8932 + tripIncrease).toLocaleString();
        }, 30000);
    </script>

<%@include file = "components/DsFin.jsp" %>
