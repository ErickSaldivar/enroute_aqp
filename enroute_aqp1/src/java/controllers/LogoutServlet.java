package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ========== SERVLET DE CIERRE DE SESIÓN ==========
 * 
 * Este Servlet implementa la funcionalidad de logout/cierre de sesión aplicando
 * los patrones de diseño y conceptos vistos en clase:
 * 
 * PATRONES APLICADOS:
 * - MVC (Model-View-Controller): Este servlet actúa como CONTROLADOR
 * - Gestión de sesiones HTTP para control de acceso
 * 
 * CONCEPTOS DE SERVLETS IMPLEMENTADOS:
 * - Manejo de sesiones HTTP (HttpSession)
 * - Invalidación de sesiones para seguridad
 * - Redirección después del logout
 * - Manejo de peticiones GET y POST
 * 
 * FUNCIONALIDADES CORE DEL NEGOCIO:
 * - Control de acceso: cierre seguro de sesiones de usuario
 * - Limpieza de datos de sesión para prevenir accesos no autorizados
 * - Redirección a página de inicio después del logout
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance del Proyecto Final
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    /**
     * ========== MÉTODO processRequest - MANEJO GENÉRICO DE REQUESTS ==========
     * 
     * Método auxiliar para procesar requests HTTP de manera genérica.
     * Principalmente usado para debugging y testing del servlet.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LogoutServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LogoutServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * ========== MÉTODO doGet - PROCESAMIENTO DE LOGOUT ==========
     * 
     * Maneja las peticiones GET para cerrar sesión del usuario implementando:
     * - Obtención de la sesión HTTP actual
     * - Invalidación segura de la sesión
     * - Limpieza de todos los atributos de sesión
     * - Redirección a la página de inicio
     * 
     * CONCEPTOS DE SEGURIDAD APLICADOS:
     * - Invalidación completa de sesión HTTP
     * - Prevención de accesos no autorizados después del logout
     * - Limpieza de datos sensibles de la sesión
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse para redirección
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ========== PASO 1: OBTENER SESIÓN ACTUAL ==========
        // Obtener la sesión actual sin crear una nueva si no existe
        // El parámetro false evita crear una sesión nueva
        HttpSession session = request.getSession(false);
        
        // ========== PASO 2: INVALIDAR SESIÓN SI EXISTE ==========
        if (session != null) {
            // Invalidar la sesión - esto elimina todos los atributos
            // y marca la sesión como inválida para futuras peticiones
            session.invalidate();
            System.out.println("DEBUG LogoutServlet - Sesión invalidada exitosamente");
        } else {
            System.out.println("DEBUG LogoutServlet - No había sesión activa para invalidar");
        }
        
        // ========== PASO 3: REDIRECCIÓN DESPUÉS DEL LOGOUT ==========
        // Redirigir al index con mensaje de logout exitoso
        // Esto asegura que el usuario vuelva a la página de inicio
        response.sendRedirect("index.html?logout=success");
    }

    /**
     * ========== MÉTODO doPost - MANEJO DE PETICIONES POST ==========
     * 
     * Maneja las peticiones POST redirigiendo al método doGet.
     * Esto permite que el logout funcione tanto con GET como POST.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al método doGet para mantener consistencia
        // en el procesamiento del logout
        doGet(request, response);
    }

    /**
     * ========== INFORMACIÓN DEL SERVLET ==========
     * 
     * Proporciona información descriptiva sobre este servlet.
     * Útil para documentación y debugging.
     * 
     * @return String Descripción del servlet
     */
    @Override
    public String getServletInfo() {
        return "LogoutServlet - Maneja el cierre seguro de sesiones de usuario aplicando control de acceso";
    }
}
