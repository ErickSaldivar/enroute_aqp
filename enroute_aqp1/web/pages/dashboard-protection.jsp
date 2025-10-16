<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Verificar si el usuario está logueado
    if (session.getAttribute("userEmail") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Obtener información del usuario (sin redeclarar variables que ya están en DsInicio.jsp)
    String protectionUserEmail = (String) session.getAttribute("userEmail");
    Boolean protectionIsAdminObj = (Boolean) session.getAttribute("isAdmin");
    
    // Verificación doble para administradores (igual que en DsInicio.jsp)
    boolean protectionIsAdmin = false;
    
    // Estrategia 1: Verificar por email específico
    if (protectionUserEmail != null) {
        String emailLower = protectionUserEmail.toLowerCase();
        if ("jordi@admin.com".equals(emailLower) || "admin@enroute.com".equals(emailLower)) {
            protectionIsAdmin = true;
            System.out.println("DEBUG dashboard-protection.jsp - Usuario admin detectado por email: " + emailLower);
        }
    }
    
    // Estrategia 2: Verificar por valor de sesión si no se detectó por email
    if (!protectionIsAdmin && protectionIsAdminObj != null) {
        protectionIsAdmin = protectionIsAdminObj.booleanValue();
        System.out.println("DEBUG dashboard-protection.jsp - Usuario admin detectado por sesión: " + protectionIsAdmin);
    }
    
    System.out.println("DEBUG dashboard-protection.jsp - isAdmin final: " + protectionIsAdmin);
    
    // Verificar si el usuario es administrador
    if (!protectionIsAdmin) {
        System.out.println("DEBUG dashboard-protection.jsp - Acceso denegado, redirigiendo a mapa.jsp");
        response.sendRedirect("mapa.jsp");
        return;
    }
    
    System.out.println("DEBUG dashboard-protection.jsp - Acceso permitido al dashboard");
%>