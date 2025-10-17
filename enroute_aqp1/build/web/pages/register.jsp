<%-- 
    ========== PÁGINA DE REGISTRO DE USUARIOS - JSP ==========
    
    Esta página JSP implementa el formulario de registro del sistema aplicando
    los conceptos y patrones vistos en clase:
    
    CONCEPTOS JSP IMPLEMENTADOS:
    - Directivas JSP: @page, @taglib para configuración
    - JSTL (Java Standard Tag Library): uso de <c:if>, <c:choose>, <c:when>, <c:otherwise>
    - Expression Language (EL): ${not empty param.error}, ${param.error eq 'value'}
    - Manejo de parámetros de request para mensajes de error
    - Integración con Servlets mediante formularios HTML
    
    PATRONES APLICADOS:
    - MVC: Esta página actúa como VISTA del patrón MVC
    - Separación de responsabilidades: Vista separada de lógica de negocio
    - Reutilización: Uso de CSS externo y Bootstrap
    
    FUNCIONALIDADES CORE DEL NEGOCIO:
    - Registro de usuarios: formulario de alta de nuevos usuarios
    - Validación de datos mediante RegisterServlet
    - Manejo de mensajes de error específicos
    - Redirección después de registro exitoso
    
    INTEGRACIÓN CON FRAMEWORKS FRONTEND:
    - Bootstrap 5 para diseño responsivo y componentes UI
    - CSS personalizado para estilos específicos
    - Formularios HTML5 con validación client-side
    
    CONCEPTOS AVANZADOS DE JSTL Y EL APLICADOS:
    - <c:if>: Renderizado condicional de mensajes de error
    - <c:choose>, <c:when>, <c:otherwise>: Lógica condicional múltiple
    - ${not empty param.error}: Verificación de existencia de parámetros
    - ${param.error eq 'value'}: Comparación de valores de parámetros
    
    @author Equipo EnRoute AQP
    @version 2.0 - Segundo Avance del Proyecto Final
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- ========== IMPORTACIÓN DE JSTL CORE ========== --%>
<%-- Importar JSTL Core para usar etiquetas avanzadas como <c:choose> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Rutas Arequipa</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Styles -->
    <link rel="stylesheet" href="../css/custom-styles.css">
</head>
<body class="d-flex justify-content-center align-items-center min-vh-100 bg-light-custom">

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <!-- Header Section -->
                <div class="text-center mb-4">
                    <img src="../img/icon_img.png" alt="Logo" width="60" height="60" class="mb-2"/>
                    <h1 class="h3 fw-bold text-dark-custom">Rutas Arequipa</h1>
                    <p class="text-muted-custom">Crea tu cuenta para acceder al sistema</p>
                </div>

                <!-- Registration Card -->
                <div class="card shadow-sm border-0 rounded-4 auth-card">
                    <div class="card-body p-4">
                        <h2 class="h4 fw-bold mb-4 text-dark">Crear Cuenta</h2>
                        
                        <%-- ========== MANEJO AVANZADO DE ERRORES CON JSTL ========== --%>
                        <%-- Uso de JSTL <c:if> para verificar existencia de parámetros de error --%>
                        <%-- EL: ${not empty param.error} verifica si existe parámetro error en URL --%>
                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger text-center mb-3" role="alert">
                                <%-- ========== LÓGICA CONDICIONAL MÚLTIPLE CON <c:choose> ========== --%>
                                <%-- Uso de <c:choose>, <c:when>, <c:otherwise> para múltiples condiciones --%>
                                <%-- Esto es equivalente a switch-case en Java --%>
                                <c:choose>
                                    <%-- EL: ${param.error eq 'empty'} compara valor del parámetro --%>
                                    <c:when test="${param.error eq 'empty'}">
                                        Todos los campos son obligatorios.
                                    </c:when>
                                    <%-- EL: ${param.error eq 'duplicate'} verifica error de duplicado --%>
                                    <c:when test="${param.error eq 'duplicate'}">
                                        Este correo electrónico ya está registrado.
                                    </c:when>
                                    <%-- EL: ${param.error eq 'database'} verifica error de BD --%>
                                    <c:when test="${param.error eq 'database'}">
                                        Error en la base de datos. Inténtalo de nuevo.
                                    </c:when>
                                    <%-- <c:otherwise> maneja cualquier otro caso no contemplado --%>
                                    <c:otherwise>
                                        Error desconocido. Inténtalo de nuevo.
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        
                        <%-- ========== MANEJO DE ERRORES DE REQUEST SCOPE ========== --%>
                        <%-- Uso adicional de JSTL para errores enviados desde el Servlet --%>
                        <%-- EL: ${not empty requestScope.error} verifica atributos de request --%>
                        <c:if test="${not empty requestScope.error}">
                            <div class="alert alert-danger text-center mb-3" role="alert">
                                <%-- <c:out> para escape seguro del contenido dinámico --%>
                                <%-- EL: ${requestScope.error} accede al atributo error del request --%>
                                <c:out value="${requestScope.error}" />
                            </div>
                        </c:if>
                        
                        <%-- ========== FORMULARIO DE REGISTRO CON INTEGRACIÓN SERVLET ========== --%>
                        <%-- EL: ${pageContext.request.contextPath} obtiene el contexto de la aplicación --%>
                        <form method="post" action="${pageContext.request.contextPath}/RegisterServlet">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="nombre" class="form-label fw-medium text-dark">Nombre</label>
                                    <input type="text" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                           id="nombre" name="nombre" placeholder="Tu nombre" required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="apellido" class="form-label fw-medium text-dark">Apellido</label>
                                    <input type="text" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                           id="apellido" name="apellido" placeholder="Tu apellido" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label fw-medium text-dark">Correo electrónico</label>
                                <input type="email" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                       id="email" name="email" placeholder="correo@ejemplo.com" required>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label fw-medium text-dark">Contraseña</label>
                                    <input type="password" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                           id="password" name="password" placeholder="••••••••" required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="confirmPassword" class="form-label fw-medium text-dark">Confirmar Contraseña</label>
                                    <input type="password" class="form-control form-control-lg border-1 rounded-3 form-custom" 
                                           id="confirmPassword" name="confirm-password" placeholder="••••••••" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="rol" class="form-label fw-medium text-dark">Tipo de Usuario</label>
                                <select class="form-select form-select-lg border-1 rounded-3 form-custom" 
                                        id="rol" name="rol" required>
                                    <option value="">Selecciona un tipo</option>
                                    <option value="pasajero">Pasajero</option>
                                    <option value="conductor">Conductor</option>
                                </select>
                            </div>
                            
                            <div class="form-check mb-4">
                                <input class="form-check-input" type="checkbox" id="terms" required>
                                <label class="form-check-label text-muted-custom" for="terms">
                                    Acepto los <a href="#" class="text-decoration-none text-primary-custom">términos y condiciones</a>
                                </label>
                            </div>
                            
                            <button type="submit" class="btn btn-custom btn-lg w-100 fw-bold rounded-3 mt-3">
                                Crear Cuenta
                            </button>
                        </form>

                        <p class="text-center mt-4 mb-0 text-muted-custom">
                            ¿Ya tienes una cuenta? 
                            <a href="login.jsp" class="text-decoration-none fw-bold text-primary-custom">Iniciar sesión</a>
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
