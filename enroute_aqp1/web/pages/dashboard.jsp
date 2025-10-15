<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.userEmail}">
    <c:redirect url="login.jsp" />
</c:if>
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
            background: #2c2c2c;
            }
            .icon-circle {
                display: flex;
                align-items: center;
                justify-content: center;
                width: 60px;
                height: 60px;
                border-radius: 50%;
                margin: 0 auto 20px auto;
                background: white;
                box-shadow: 0 2px 8px var(--color-principal);
            }
            .icon-circle svg {
                width: 32px;
                height: 32px;
                fill: black;
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
    <nav class="navbar">
        <div class="navbar-logo">
              <img src="../img/icon_img.png" alt="Logo" style="width: 32px; height: 32px;">
            <span>Enroute AQP</span>
        </div>
        <div class="navbar-user">
            <span class="navbar-user-name">¡Bienvenido ${sessionScope.userName}!</span>
            <div class="navbar-avatar">
                <img src="https://randomuser.me/api/portraits/men/44.jpg" alt="Avatar" />
            </div>
        </div>
    </nav>
    <div class="dashboard-container">
        
        
        <div class="dashboard-grid">
            <div class="dashboard-card">
                    <div class="icon-circle">
                       
                        <svg viewBox="0 0 24 24"><path d="M20.5 3l-5.5 2.5-6-2.5-5.5 2.5v16l6-2.5 6 2.5 5.5-2.5v-16zm-6.5 17.11l-6-2.5v-13.61l6 2.5v13.61zm7-2.5l-5 2.27v-13.61l5-2.27v13.61z"/></svg>
                    </div>
                <h3>Rutas Disponibles</h3>
                <p>Explora las rutas turísticas más populares de Arequipa</p>
                <button class="btn btn-primary">Ver Rutas</button>
            </div>
            
            <div class="dashboard-card">
                    <div class="icon-circle">
                        
                        <svg viewBox="0 0 24 24"><path d="M12 12c2.7 0 5-2.3 5-5s-2.3-5-5-5-5 2.3-5 5 2.3 5 5 5zm0 2c-3.3 0-10 1.7-10 5v3h20v-3c0-3.3-6.7-5-10-5z"/></svg>
                    </div>
                <h3>Mi Perfil</h3>
                <p>Gestiona tu información personal y preferencias</p>
                <button class="btn btn-primary">Editar Perfil</button>
            </div>
            
            <div class="dashboard-card">
                    <div class="icon-circle">
                        
                        <svg viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32c-5.05-4.6-8.35-7.6-8.35-11.07 0-2.54 2.07-4.61 4.61-4.61 1.74 0 3.41 1.01 4.19 2.61.78-1.6 2.45-2.61 4.19-2.61 2.54 0 4.61 2.07 4.61 4.61 0 3.47-3.3 6.47-8.35 11.07z"/></svg>
                    </div>
                <h3>Favoritos</h3>
                <p>Revisa tus rutas y lugares favoritos guardados</p>
                <button class="btn btn-primary">Ver Favoritos</button>
            </div>
            
            <div class="dashboard-card">
                    <div class="icon-circle">
                        
                        <svg viewBox="0 0 24 24"><path d="M12 8v5l4.28 2.54.72-1.21-3.5-2.08V8zm0-6c-5.52 0-10 4.48-10 10s4.48 10 10 10 10-4.48 10-10-4.48-10-10-10zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/></svg>
                    </div>
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
