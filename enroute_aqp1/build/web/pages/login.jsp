<%-- 
    ========== PÁGINA DE INICIO DE SESIÓN - JSP ==========
    
    Esta página JSP implementa el formulario de login del sistema aplicando
    los conceptos y patrones vistos en clase:
    
    CONCEPTOS JSP IMPLEMENTADOS:
    - Directivas JSP: @page, @taglib para configuración
    - JSTL (Java Standard Tag Library): uso de <c:if> y <c:out>
    - Expression Language (EL): ${requestScope.error}, ${param.registration}
    - Manejo de parámetros de request y atributos
    - Integración con Servlets mediante formularios HTML
    
    PATRONES APLICADOS:
    - MVC: Esta página actúa como VISTA del patrón MVC
    - Separación de responsabilidades: Vista separada de lógica de negocio
    - Reutilización: Uso de CSS externo y Bootstrap
    
    FUNCIONALIDADES CORE DEL NEGOCIO:
    - Control de acceso: formulario de autenticación
    - Validación de credenciales mediante LoginServlet
    - Manejo de mensajes de error y éxito
    - Redirección después de login exitoso
    
    INTEGRACIÓN CON FRAMEWORKS FRONTEND:
    - Bootstrap 5 para diseño responsivo y componentes UI
    - CSS personalizado para estilos específicos
    - Formularios HTML5 con validación
    
    CONCEPTOS DE JSTL Y EL APLICADOS:
    - <c:if>: Renderizado condicional de mensajes
    - <c:out>: Escape seguro de contenido dinámico
    - ${requestScope.error}: Acceso a atributos de request
    - ${param.registration}: Acceso a parámetros de URL
    
    @author Equipo EnRoute AQP
    @version 2.0 - Segundo Avance del Proyecto Final
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- ========== IMPORTACIÓN DE JSTL CORE ========== --%>
<%-- Importar JSTL Core para usar etiquetas como <c:if> y <c:out> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Rutas Arequipa</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Styles -->
    <link rel="stylesheet" href="../css/custom-styles.css">
</head>
<body class="d-flex justify-content-center align-items-center min-vh-100 bg-light-custom">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <!-- Logo Section -->
                <div class="text-center mb-4">
                    <img src="../img/icon_img.png" alt="Logo" width="60" height="60" class="mb-2"/>
                    <h1 class="h3 fw-bold text-dark-custom">Rutas Arequipa</h1>
                </div>

                <!-- Login Card -->
                <div class="card shadow-sm border-0 rounded-4 auth-card">
                    <div class="card-body p-4">
                        <h2 class="h4 fw-bold mb-4 text-dark">Iniciar Sesión</h2>
                        
                        <%-- ========== MANEJO DE MENSAJES DE ERROR CON JSTL ========== --%>
                        <%-- Uso de JSTL <c:if> para renderizado condicional de errores --%>
                        <%-- EL: ${not empty requestScope.error} verifica si existe error en request --%>
                        <c:if test="${not empty requestScope.error}">
                            <div class="alert alert-danger text-center mb-3" role="alert">
                                <%-- Uso de <c:out> para escape seguro del contenido dinámico --%>
                                <%-- EL: ${requestScope.error} accede al atributo error del request --%>
                                <c:out value="${requestScope.error}" />
                            </div>
                        </c:if>
                        
                        <%-- ========== MANEJO DE MENSAJES DE ÉXITO CON JSTL ========== --%>
                        <%-- Uso de JSTL <c:if> para mostrar mensaje de registro exitoso --%>
                        <%-- EL: ${param.registration eq 'success'} verifica parámetro URL --%>
                        <c:if test="${param.registration eq 'success'}">
                            <div class="alert alert-success text-center mb-3" role="alert">
                                ¡Registro exitoso! Ahora puedes iniciar sesión.
                            </div>
                        </c:if>
                        
                        <%-- ========== FORMULARIO DE LOGIN CON INTEGRACIÓN SERVLET ========== --%>
                        <%-- EL: ${pageContext.request.contextPath} obtiene el contexto de la aplicación --%>
                        <%-- Esto asegura que la URL del action sea correcta independientemente del contexto --%>
                        <form method="post" action="${pageContext.request.contextPath}/LoginServlet">
                            <div class="mb-3">
                                <label for="email" class="form-label fw-medium text-dark">Correo electrónico</label>
                                <input type="email" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                       id="email" name="email" placeholder="correo@ejemplo.com" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label fw-medium text-dark">Contraseña</label>
                                <input type="password" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                       id="password" name="password" placeholder="••••••••" required>
                            </div>
                            
                            <div class="text-end mb-3">
                                <a href="#" class="text-decoration-none small text-primary-custom">¿Olvidaste tu contraseña?</a>
                            </div>
                            
                            <button type="submit" class="btn btn-custom btn-lg w-100 fw-bold rounded-3 mt-3">
                                Iniciar Sesión
                            </button>
                        </form>

                        <p class="text-center mt-4 mb-0 text-muted-custom">
                            ¿No tienes una cuenta? 
                            <a href="register.jsp" class="text-decoration-none fw-bold text-primary-custom">Crear cuenta</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
