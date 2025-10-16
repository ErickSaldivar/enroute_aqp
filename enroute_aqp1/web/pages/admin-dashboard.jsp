<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    // Verificar sesión y rol de administrador
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null || !userRole.equals("administrador")) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
    
    String userName = (String) session.getAttribute("userName");
    String userLastName = (String) session.getAttribute("userLastName");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración - Enroute AQP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .admin-card {
            transition: transform 0.2s;
        }
        .admin-card:hover {
            transform: translateY(-5px);
        }
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
    </style>
</head>
<body class="bg-light">
    <!-- Incluir el componente de navegación -->
    <%@ include file="components/DsInicio.jsp" %>
    
    <div class="container-fluid">
        <div class="row">
            <!-- Contenido principal -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Panel de Administración</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="bi bi-download"></i> Exportar
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Estadísticas generales -->
                <div class="row mb-4">
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-primary shadow h-100 py-2 stats-card text-white">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-uppercase mb-1">Total Usuarios</div>
                                        <div class="h5 mb-0 font-weight-bold">1,234</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-people fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-success shadow h-100 py-2" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);">
                            <div class="card-body text-white">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-uppercase mb-1">Rutas Activas</div>
                                        <div class="h5 mb-0 font-weight-bold">45</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-bus-front fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-info shadow h-100 py-2" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <div class="card-body text-white">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-uppercase mb-1">Viajes Hoy</div>
                                        <div class="h5 mb-0 font-weight-bold">156</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-calendar-check fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card border-left-warning shadow h-100 py-2" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <div class="card-body text-white">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-uppercase mb-1">Reportes Pendientes</div>
                                        <div class="h5 mb-0 font-weight-bold">8</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-exclamation-triangle fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Acciones rápidas -->
                <div class="row mb-4">
                    <div class="col-lg-4 mb-3">
                        <div class="card admin-card shadow">
                            <div class="card-body text-center">
                                <i class="bi bi-people-fill text-primary mb-3" style="font-size: 3rem;"></i>
                                <h5 class="card-title">Gestión de Usuarios</h5>
                                <p class="card-text">Administrar usuarios, roles y permisos del sistema.</p>
                                <a href="admin-users.jsp" class="btn btn-primary">
                                    <i class="bi bi-arrow-right"></i> Acceder
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4 mb-3">
                        <div class="card admin-card shadow">
                            <div class="card-body text-center">
                                <i class="bi bi-map-fill text-success mb-3" style="font-size: 3rem;"></i>
                                <h5 class="card-title">Gestión de Rutas</h5>
                                <p class="card-text">Crear, editar y administrar las rutas de transporte.</p>
                                <a href="admin-routes.jsp" class="btn btn-success">
                                    <i class="bi bi-arrow-right"></i> Acceder
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4 mb-3">
                        <div class="card admin-card shadow">
                            <div class="card-body text-center">
                                <i class="bi bi-graph-up text-info mb-3" style="font-size: 3rem;"></i>
                                <h5 class="card-title">Reportes y Estadísticas</h5>
                                <p class="card-text">Ver reportes detallados y estadísticas del sistema.</p>
                                <a href="admin-reports.jsp" class="btn btn-info">
                                    <i class="bi bi-arrow-right"></i> Acceder
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actividad reciente -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Actividad Reciente</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>Fecha</th>
                                        <th>Usuario</th>
                                        <th>Acción</th>
                                        <th>Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>2024-01-15 10:30</td>
                                        <td>Juan Pérez</td>
                                        <td>Registro de nuevo usuario</td>
                                        <td><span class="badge bg-success">Completado</span></td>
                                    </tr>
                                    <tr>
                                        <td>2024-01-15 09:15</td>
                                        <td>María García</td>
                                        <td>Actualización de ruta</td>
                                        <td><span class="badge bg-warning">Pendiente</span></td>
                                    </tr>
                                    <tr>
                                        <td>2024-01-15 08:45</td>
                                        <td>Carlos López</td>
                                        <td>Reporte de incidencia</td>
                                        <td><span class="badge bg-info">En revisión</span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>