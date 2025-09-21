<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglibs uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    <link rel="stylesheet" href="../css/landing-styles.css">
    <style>
        .dashboard-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .welcome-section {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 40px;
            border-radius: 10px;
            margin-bottom: 30px;
            text-align: center;
        }
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .dashboard-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .logout-btn {
            background-color: #d32f2f;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
        }
        .logout-btn:hover {
            background-color: #b71c1c;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <div class="welcome-section">
            <h1>¡Bienvenido a Enroute AQP!</h1>
            <p>Hola, <%= userEmail %>. Estás conectado exitosamente.</p>
            <p>Descubre las mejores rutas turísticas de Arequipa</p>
        </div>
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <h3>Rutas Disponibles</h3>
                <p>Explora las rutas turísticas más populares de Arequipa</p>
                <button class="btn btn-primary">Ver Rutas</button>
            </div>
            
            <div class="dashboard-card">
                <h3>Mi Perfil</h3>
                <p>Gestiona tu información personal y preferencias</p>
                <button class="btn btn-primary">Editar Perfil</button>
            </div>
            
            <div class="dashboard-card">
                <h3>Favoritos</h3>
                <p>Revisa tus rutas y lugares favoritos guardados</p>
                <button class="btn btn-primary">Ver Favoritos</button>
            </div>
            
            <div class="dashboard-card">
                <h3>Historial</h3>
                <p>Consulta tu historial de rutas visitadas</p>
                <button class="btn btn-primary">Ver Historial</button>
            </div>
        </div>
        
        <div style="text-align: center; margin-top: 40px;">
            <a href="../LogoutServlet" class="logout-btn">Cerrar Sesión</a>
        </div>
    </div>
</body>
</html>
