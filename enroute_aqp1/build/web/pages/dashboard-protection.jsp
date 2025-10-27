<%-- 
    ========== COMPONENTE DE PROTECCIÓN DE DASHBOARD ==========
    
    Este archivo JSP implementa la lógica de protección de acceso al dashboard
    aplicando los conceptos y patrones vistos en clase:
    
    CONCEPTOS JSP IMPLEMENTADOS:
    - Scriptlets JSP para lógica de negocio
    - Manejo de sesiones HTTP (HttpSession)
    - Atributos de sesión para persistencia de datos
    - Redirección condicional basada en permisos
    
    PATRONES APLICADOS:
    - MVC: Actúa como parte del CONTROLADOR para validación de acceso
    - Separación de responsabilidades: Lógica de seguridad separada
    - Reutilización: Componente reutilizable para protección
    
    FUNCIONALIDADES CORE DEL NEGOCIO:
    - Control de acceso: verificación de autenticación
    - Validación de roles: solo administradores pueden acceder
    - Estrategia dual de verificación de admin (email + sesión)
    - Redirección automática según permisos
    
    CONCEPTOS DE SEGURIDAD APLICADOS:
    - Verificación de sesión activa
    - Validación de roles de usuario
    - Redirección segura en caso de acceso no autorizado
    
    @author Equipo EnRoute AQP
    @version 2.0 - Segundo Avance del Proyecto Final
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // ========== VERIFICACIÓN DE AUTENTICACIÓN ==========
    // Verificar si el usuario está logueado mediante atributo de sesión
    // Si no hay sesión activa, redirigir al login
    if (session.getAttribute("userEmail") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // ========== OBTENCIÓN DE DATOS DE SESIÓN ==========
    // Obtener información del usuario desde los atributos de sesión
    // (sin redeclarar variables que ya están en DsInicio.jsp)
    String protectionUserEmail = (String) session.getAttribute("userEmail");
    Boolean protectionIsAdminObj = (Boolean) session.getAttribute("isAdmin");
    
    // ========== VERIFICACIÓN DUAL DE PERMISOS DE ADMINISTRADOR ==========
    // Implementar estrategia dual para verificar permisos de admin
    // Esto asegura robustez en la validación de roles
    boolean protectionIsAdmin = false;
    
    // ========== ESTRATEGIA 1: VERIFICACIÓN POR EMAIL ESPECÍFICO ==========
    // Verificar por email específico (lista de administradores conocidos)
    // Esta es una estrategia robusta para casos donde el flag de sesión falle
    if (protectionUserEmail != null) {
        String emailLower = protectionUserEmail.toLowerCase();
        if ("jordi@admin.com".equals(emailLower) || "erick@admin.com".equals(emailLower) || 
            "andre@admin.com".equals(emailLower) || "admin@enroute.com".equals(emailLower)) {
            protectionIsAdmin = true;
            System.out.println("DEBUG dashboard-protection.jsp - Usuario admin detectado por email: " + emailLower);
        }
    }
    
    // ========== ESTRATEGIA 2: VERIFICACIÓN POR ATRIBUTO DE SESIÓN ==========
    // Verificar por valor de sesión si no se detectó por email
    // Esto permite flexibilidad para administradores dinámicos
    if (!protectionIsAdmin && protectionIsAdminObj != null) {
        protectionIsAdmin = protectionIsAdminObj.booleanValue();
        System.out.println("DEBUG dashboard-protection.jsp - Usuario admin detectado por sesión: " + protectionIsAdmin);
    }
    
    System.out.println("DEBUG dashboard-protection.jsp - isAdmin final: " + protectionIsAdmin);
    
    // ========== CONTROL DE ACCESO FINAL ==========
    // Verificar si el usuario es administrador
    // Si no es admin, redirigir al mapa (página de usuario normal)
    if (!protectionIsAdmin) {
        System.out.println("DEBUG dashboard-protection.jsp - Acceso denegado, redirigiendo a mapa.jsp");
        response.sendRedirect("mapa.jsp");
        return;
    }
    
    System.out.println("DEBUG dashboard-protection.jsp - Acceso permitido al dashboard");
%>