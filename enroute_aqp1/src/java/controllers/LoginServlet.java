package controllers;

import model.LoginBean;
import dao.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginServlet - Controlador para la autenticación de usuarios
 * 
 * Este servlet implementa el patrón MVC como controlador para el proceso de login.
 * Maneja la autenticación de usuarios y la gestión de sesiones según roles.
 * 
 * Funcionalidades implementadas:
 * - Control de acceso: autenticación y validación de usuarios
 * - Gestión de sesiones HTTP
 * - Redirección dinámica según roles (admin/usuario regular)
 * - Validación de parámetros de entrada
 * - Manejo de errores y excepciones
 * 
 * Patrones aplicados:
 * - MVC: Actúa como controlador entre la vista (login.jsp) y el modelo (LoginBean)
 * - DAO: Utiliza DBConnection para acceso a datos
 * - DTO: Usa LoginBean como objeto de transferencia de datos
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Procesa las peticiones HTTP GET y POST
     * Método base que puede ser extendido para funcionalidades comunes
     * 
     * @param request objeto HttpServletRequest con los parámetros de la petición
     * @param response objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Método base - implementación específica en doGet/doPost
    }

    /**
     * Maneja las peticiones HTTP GET
     * Redirige al método processRequest para manejo uniforme
     * 
     * @param request objeto HttpServletRequest con los parámetros de la petición
     * @param response objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja las peticiones HTTP POST para el proceso de autenticación
     * 
     * Este método implementa la lógica principal del login:
     * 1. Extrae y valida los parámetros del formulario (email, password)
     * 2. Crea un LoginBean (DTO) con los datos recibidos
     * 3. Realiza la consulta a la base de datos usando el patrón DAO
     * 4. Valida las credenciales con hash SHA-256
     * 5. Crea la sesión HTTP y establece atributos de usuario
     * 6. Redirige según el rol del usuario (admin -> dashboard, usuario -> mapa)
     * 
     * Uso de Servlets - Parámetros y Atributos:
     * - request.getParameter(): Obtiene datos del formulario HTML
     * - session.setAttribute(): Almacena información del usuario en la sesión
     * - response.sendRedirect(): Redirige a diferentes páginas según el resultado
     * 
     * @param request objeto HttpServletRequest con los parámetros del formulario
     * @param response objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ========== PASO 1: EXTRACCIÓN Y VALIDACIÓN DE PARÁMETROS ==========
        // Instanciar LoginBean (DTO - Data Transfer Object)
        // El patrón DTO encapsula los datos de transferencia entre capas
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(request.getParameter("email"));      // Parámetro del formulario HTML
        loginBean.setPassword(request.getParameter("password")); // Parámetro del formulario HTML
        
        // Validaciones básicas de entrada
        // Implementa validación de negocio antes del acceso a datos
        if (loginBean.getEmail() == null || loginBean.getPassword() == null || 
            loginBean.getEmail().trim().isEmpty() || loginBean.getPassword().trim().isEmpty()) {
            // Redirección con parámetro de error para mostrar mensaje en la vista
            response.sendRedirect("pages/login.jsp?error=empty");
            return;
        }
        
        // ========== PASO 2: ACCESO A DATOS USANDO PATRÓN DAO ==========
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Obtener conexión usando el patrón DAO (Data Access Object)
            // DBConnection encapsula la lógica de conexión a la base de datos
            conn = DBConnection.getConnection();
            
            // Hash de la contraseña para comparar con la BD
            // Implementa seguridad mediante encriptación SHA-256
            String hashedPassword = hashPassword(loginBean.getPassword());
            
            // Consulta SQL preparada para prevenir inyección SQL
            String sql = "SELECT id_usuario, nombre, apellido, es_admin FROM usuarios WHERE email = ? AND password_hash = ?";
            System.out.println("DEBUG LoginServlet - SQL Query: " + sql);
            System.out.println("DEBUG LoginServlet - Email parameter: " + loginBean.getEmail());
            System.out.println("DEBUG LoginServlet - Hashed password: " + hashedPassword);
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginBean.getEmail());    // Parámetro 1: email
            pstmt.setString(2, hashedPassword);          // Parámetro 2: password hasheado
            
            rs = pstmt.executeQuery();
            
            // ========== PASO 3: PROCESAMIENTO DE RESULTADOS Y GESTIÓN DE SESIÓN ==========
            if (rs.next()) {
                // Login exitoso - Logs de depuración detallados
                System.out.println("DEBUG LoginServlet - Usuario encontrado en BD");
                System.out.println("DEBUG LoginServlet - ID: " + rs.getInt("id_usuario"));
                System.out.println("DEBUG LoginServlet - Nombre: " + rs.getString("nombre"));
                System.out.println("DEBUG LoginServlet - Apellido: " + rs.getString("apellido"));
                
                // ========== PASO 4: DETERMINACIÓN DE ROLES Y PERMISOS ==========
                // Manejo robusto del campo es_admin para administración de menús
                Object esAdminObj = rs.getObject("es_admin");
                boolean isAdmin = false;
                
                System.out.println("DEBUG LoginServlet - Es Admin Object: " + esAdminObj + " (type: " + (esAdminObj != null ? esAdminObj.getClass().getSimpleName() : "null") + ")");
                
                // Estrategia 1: Verificar por email específico (alternativa robusta)
                String userEmail = loginBean.getEmail().toLowerCase();
                if ("jordi@admin.com".equals(userEmail) || "erick@admin.com".equals(userEmail) || 
                    "andre@admin.com".equals(userEmail) || "admin@enroute.com".equals(userEmail)) {
                    isAdmin = true;
                    System.out.println("DEBUG LoginServlet - Usuario admin detectado por email: " + userEmail);
                }
                
                // Estrategia 2: Intentar obtener es_admin de la BD si no es null
                if (!isAdmin && esAdminObj != null) {
                    try {
                        // Intentar diferentes tipos de conversión para compatibilidad de BD
                        if (esAdminObj instanceof Boolean) {
                            isAdmin = (Boolean) esAdminObj;
                            System.out.println("DEBUG LoginServlet - Es Admin Boolean: " + isAdmin);
                        } else if (esAdminObj instanceof Number) {
                            int adminValue = ((Number) esAdminObj).intValue();
                            isAdmin = (adminValue == 1);
                            System.out.println("DEBUG LoginServlet - Es Admin Number: " + adminValue + " -> " + isAdmin);
                        } else if (esAdminObj instanceof String) {
                            String adminStr = (String) esAdminObj;
                            isAdmin = "1".equals(adminStr) || "true".equalsIgnoreCase(adminStr) || "yes".equalsIgnoreCase(adminStr);
                            System.out.println("DEBUG LoginServlet - Es Admin String: " + adminStr + " -> " + isAdmin);
                        } else {
                            // Fallback: usar rs.getBoolean()
                            isAdmin = rs.getBoolean("es_admin");
                            System.out.println("DEBUG LoginServlet - Es Admin Fallback Boolean: " + isAdmin);
                        }
                    } catch (Exception e) {
                        System.out.println("DEBUG LoginServlet - Error procesando es_admin: " + e.getMessage());
                        // En caso de error, verificar si es usuario admin por email
                        isAdmin = "jordi@admin.com".equals(userEmail) || "erick@admin.com".equals(userEmail) || 
                                 "andre@admin.com".equals(userEmail);
                    }
                }
                
                // ========== PASO 5: CREACIÓN DE SESIÓN HTTP Y ATRIBUTOS ==========
                // Gestión de sesiones HTTP para mantener estado del usuario
                HttpSession session = request.getSession();
                
                // Establecer atributos de sesión (session.setAttribute)
                // Estos atributos estarán disponibles en todas las páginas JSP
                session.setAttribute("userId", rs.getInt("id_usuario"));        // ID único del usuario
                session.setAttribute("userName", rs.getString("nombre"));        // Nombre para personalización
                session.setAttribute("userLastName", rs.getString("apellido"));  // Apellido para personalización
                session.setAttribute("userEmail", loginBean.getEmail());         // Email para identificación
                session.setAttribute("isAdmin", isAdmin);                        // Rol para control de acceso
                session.setAttribute("loginBean", loginBean);                    // DTO completo para uso posterior
                
                // Log de depuración final
                System.out.println("DEBUG LoginServlet - Email: " + loginBean.getEmail());
                System.out.println("DEBUG LoginServlet - Valor isAdmin final: " + isAdmin);
                System.out.println("DEBUG LoginServlet - Redirigiendo a: " + (isAdmin ? "dashboard.jsp" : "mapa.jsp"));
                
                // ========== PASO 6: REDIRECCIÓN DINÁMICA SEGÚN ROLES ==========
                // Administración de menús: navegación dinámica según roles o permisos
                if (isAdmin) {
                    System.out.println("DEBUG - Redirigiendo a dashboard.jsp");
                    // Admin: acceso al panel de administración (Dashboard principal)
                    response.sendRedirect("pages/dashboard.jsp");
                } else {
                    System.out.println("DEBUG - Redirigiendo a mapa.jsp");
                    // Usuario regular: acceso a funcionalidades del núcleo de negocio
                    response.sendRedirect("pages/mapa.jsp");
                }
            } else {
                // ========== MANEJO DE CREDENCIALES INCORRECTAS ==========
                // Credenciales incorrectas - redirigir con mensaje de error
                response.sendRedirect("pages/login.jsp?error=invalid");
            }
            
        } catch (SQLException e) {
            // ========== MANEJO DE ERRORES DE BASE DE DATOS ==========
            e.printStackTrace();
            // Redirigir con error de base de datos
            response.sendRedirect("pages/login.jsp?error=database");
        } finally {
            // ========== LIMPIEZA DE RECURSOS ==========
            // Cerrar recursos de base de datos para evitar memory leaks
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Método simple para hashear la contraseña
     * Debe coincidir con el método usado en RegisterServlet
     */
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password; // Fallback
        }
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet para autenticación de usuarios";
    }
}