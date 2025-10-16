<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file = "dashboard-protection.jsp" %>
<%@include file = "components/DsInicio.jsp" %>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                        <h5 class="mb-0 fw-bold">Dashboard</h5>
                    </div>
                </div>
            </div>

            <!-- Dashboard Content -->
            <div class="dashboard-container p-3 p-lg-4">
                <div class="row mb-4">
                    <div class="col-12">
                        <h2 class="fw-bold text-dark mb-1">Dashboard</h2>
                        <p class="text-muted">Bienvenido, <%= session.getAttribute("userName") %></p>
                    </div>
                </div>
        
        <style>
        .dashboard-container {
            max-width: 1400px;
            margin: 0 auto;
        }
        .kpi-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            text-align: center;
            position: relative;
            overflow: hidden;
            transition: transform 0.3s, box-shadow 0.3s;
            border: none;
        }
        
        .kpi-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
        }
        
        .kpi-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(255, 255, 255, 0.1);
            transform: translateX(-100%);
            transition: transform 0.6s;
        }
        
        .kpi-card:hover::before {
            transform: translateX(100%);
        }
        
        .kpi-number {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        }
        
        .kpi-label {
            font-size: 1rem;
            opacity: 0.9;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        /* Chart Cards con Bootstrap */
        .chart-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            border: none;
        }
        
        .chart-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }
        
        .chart-title {
            font-size: 1.2rem;
            font-weight: bold;
            color: #333;
            text-align: center;
        }
        
        .chart-container {
            position: relative;
            height: 300px;
        }
        
        /* Dashboard Cards con Bootstrap */
        .dashboard-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
            border: none;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
        }
        
        .icon-circle {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 70px;
            height: 70px;
            border-radius: 50%;
            margin: 0 auto 20px auto;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        
        .dashboard-card h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 1.3rem;
        }
        
        .dashboard-card p {
            color: #666;
            margin-bottom: 20px;
            line-height: 1.5;
        }
        
        .btn-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 25px;
            padding: 12px 25px;
            transition: all 0.3s;
            font-weight: 500;
        }
        
        .btn-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            color: white;
        }
        
        .btn-logout {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
            color: white;
            border: none;
            border-radius: 25px;
            padding: 12px 25px;
            transition: all 0.3s;
            font-weight: 500;
        }
        
        .btn-logout:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 107, 107, 0.4);
            color: white;
        }
        
        /* Navbar personalizada */
        .navbar-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
        }
        
        .navbar-brand {
            color: white !important;
            font-weight: bold;
        }
        
        .navbar-text {
            color: white !important;
        }
        
        /* Iconos Bootstrap */
        .bi {
            font-size: 2rem;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Navbar con Bootstrap -->
    <nav class="navbar navbar-expand-lg navbar-custom">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="bi bi-geo-alt-fill me-2"></i>
                EnRoute AQP
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text">
                    <i class="bi bi-person-circle me-2"></i>
                    Bienvenido, <strong><c:out value="${sessionScope.userName}"/></strong>
                </span>
            </div>
        </div>
    </nav>

    <div class="dashboard-container">
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

        <!-- Dashboard Cards con Bootstrap Grid -->
        <div class="row g-4">
            <div class="col-lg-3 col-md-6">
                <div class="card dashboard-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="icon-circle">
                            <i class="bi bi-map"></i>
                        </div>
                        <h3 class="card-title">Rutas Disponibles</h3>
                        <p class="card-text">Explora las rutas turísticas más populares de Arequipa</p>
                        <a href="rutas.jsp" class="btn btn-custom">Ver Rutas</a>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card dashboard-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="icon-circle">
                            <i class="bi bi-person"></i>
                        </div>
                        <h3 class="card-title">Mi Perfil</h3>
                        <p class="card-text">Gestiona tu información personal y preferencias</p>
                        <a href="perfil.jsp" class="btn btn-custom">Editar Perfil</a>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card dashboard-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="icon-circle">
                            <i class="bi bi-heart"></i>
                        </div>
                        <h3 class="card-title">Favoritos</h3>
                        <p class="card-text">Revisa tus rutas y lugares favoritos guardados</p>
                        <a href="favoritos.jsp" class="btn btn-custom">Ver Favoritos</a>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-6">
                <div class="card dashboard-card h-100">
                    <div class="card-body text-center p-4">
                        <div class="icon-circle">
                            <i class="bi bi-clock-history"></i>
                        </div>
                        <h3 class="card-title">Historial</h3>
                        <p class="card-text">Consulta tu historial de rutas visitadas</p>
                        <a href="historial.jsp" class="btn btn-custom">Ver Historial</a>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Logout Button -->
        <div class="text-center mt-5">
            <a href="../LogoutServlet" class="btn btn-logout btn-lg">
                <i class="bi bi-box-arrow-right me-2"></i>
                Cerrar Sesión
            </a>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

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
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
