package controllers;

import model.RegisterBean;
import dao.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ========== SERVLET DE REGISTRO DE USUARIOS ==========
 * 
 * Este Servlet implementa la funcionalidad de registro de usuarios aplicando
 * los patrones de diseño y conceptos vistos en clase:
 * 
 * PATRONES APLICADOS:
 * - MVC (Model-View-Controller): Este servlet actúa como CONTROLADOR
 * - DAO (Data Access Object): Utiliza DBConnection para acceso a datos
 * - DTO (Data Transfer Object): Usa RegisterBean para transferir datos
 * 
 * CONCEPTOS DE SERVLETS IMPLEMENTADOS:
 * - Uso de parámetros HTTP (request.getParameter)
 * - Uso de atributos de request (request.setAttribute)
 * - Redirección y forward entre páginas
 * - Manejo de sesiones HTTP
 * - Validación de datos de entrada
 * 
 * FUNCIONALIDADES CORE DEL NEGOCIO:
 * - Registro de usuarios: alta y gestión básica de datos
 * - Validación de contraseñas y datos de entrada
 * - Encriptación de contraseñas para seguridad
 * - Manejo de errores y duplicados
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance del Proyecto Final
 */
@WebServlet(name= "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {


    /**
     * ========== MÉTODO doPost - PROCESAMIENTO DE REGISTRO ==========
     * 
     * Maneja las peticiones POST del formulario de registro implementando:
     * - Extracción de parámetros HTTP del formulario
     * - Validación de datos de entrada
     * - Aplicación del patrón DTO con RegisterBean
     * - Acceso a base de datos usando patrón DAO
     * - Encriptación de contraseñas para seguridad
     * - Manejo de errores y redirecciones
     * 
     * @param request HttpServletRequest con los parámetros del formulario
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
        
        // ========== PASO 1: EXTRACCIÓN DE PARÁMETROS Y CREACIÓN DE DTO ==========
        // Instanciar RegisterBean (DTO - Data Transfer Object)
        // El patrón DTO encapsula los datos de transferencia entre capas
        RegisterBean registerBean = new RegisterBean();
        registerBean.setNombre(request.getParameter("nombre"));           // Parámetro: nombre del usuario
        registerBean.setApellido(request.getParameter("apellido"));       // Parámetro: apellido del usuario
        registerBean.setEmail(request.getParameter("email"));             // Parámetro: email del usuario
        registerBean.setPassword(request.getParameter("password"));       // Parámetro: contraseña del usuario
        String confirmPassword = request.getParameter("confirm-password"); // Parámetro: confirmación de contraseña
       
        
        // ========== PASO 2: VALIDACIONES DE NEGOCIO ==========
        // Validaciones básicas de entrada - implementa lógica de negocio
        if (registerBean.getNombre() == null || registerBean.getApellido() == null || 
            registerBean.getEmail() == null || registerBean.getPassword() == null ||
            registerBean.getNombre().isEmpty() || registerBean.getApellido().isEmpty() || 
            registerBean.getEmail().isEmpty() || registerBean.getPassword().isEmpty()) {
            // Redirección con parámetro de error para mostrar mensaje en la vista
            response.sendRedirect("pages/register.jsp?error=empty");
            return;
        }
        
        // Validación de coincidencia de contraseñas
        if (!registerBean.getPassword().equals(confirmPassword)) {
            // Uso de atributos de request para pasar datos a la vista
            request.setAttribute("error", "Las contraseñas no coinciden");
            // Forward a la página JSP manteniendo los datos del request
            RequestDispatcher rd = request.getRequestDispatcher("pages/register.jsp");
            rd.forward(request, response);
            return;
        }
        
        // ========== PASO 3: ACCESO A DATOS USANDO PATRÓN DAO ==========
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Obtener conexión usando el patrón DAO (Data Access Object)
            // DBConnection encapsula la lógica de conexión a la base de datos
            conn = DBConnection.getConnection();
            
            // ========== PASO 4: SEGURIDAD - ENCRIPTACIÓN DE CONTRASEÑA ==========
            // Hashear la contraseña para almacenamiento seguro
            String hashedPassword = hashPassword(registerBean.getPassword());
            
            // ========== PASO 5: INSERCIÓN EN BASE DE DATOS ==========
            // Consulta SQL preparada para prevenir inyección SQL
            String sql = "INSERT INTO usuarios (nombre, apellido, email, password_hash, fecha_registro) VALUES(?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, registerBean.getNombre());    // Parámetro 1: nombre
            pstmt.setString(2, registerBean.getApellido());  // Parámetro 2: apellido
            pstmt.setString(3, registerBean.getEmail());     // Parámetro 3: email
            pstmt.setString(4, hashedPassword);              // Parámetro 4: contraseña hasheada
            
            int rowsAffected = pstmt.executeUpdate();
            
            // ========== PASO 6: PROCESAMIENTO DE RESULTADOS ==========
            if (rowsAffected > 0) {
                // Registro exitoso - redirigir a login con mensaje de éxito
                response.sendRedirect("pages/login.jsp?registration=success");
            } else {
                // Error en el registro - redirigir con mensaje de error
                response.sendRedirect("pages/register.jsp?error=database");
            }  
        } catch (Exception e) {
            // ========== MANEJO DE ERRORES DE BASE DE DATOS ==========
            e.printStackTrace();
            // Verificar si es error de duplicado (email ya existe)
            if (e.getMessage().contains("Duplicate")) {
                response.sendRedirect("pages/register.jsp?error=duplicate");
            } else {
                response.sendRedirect("pages/register.jsp?error=unknown");
            }
        } finally {
            // ========== LIMPIEZA DE RECURSOS ==========
            // Cerrar recursos de base de datos para evitar memory leaks
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * ========== MÉTODO DE ENCRIPTACIÓN DE CONTRASEÑAS ==========
     * 
     * Implementa seguridad básica mediante hash SHA-256 de contraseñas.
     * En un entorno de producción se recomienda usar BCrypt o Argon2.
     * 
     * CONCEPTOS DE SEGURIDAD APLICADOS:
     * - Encriptación unidireccional de contraseñas
     * - Uso de algoritmos de hash seguros
     * - Prevención de almacenamiento de contraseñas en texto plano
     * 
     * @param password Contraseña en texto plano a encriptar
     * @return String Hash SHA-256 de la contraseña
     */
    private String hashPassword(String password) {
        // Por simplicidad, usamos un hash básico SHA-256
        // En producción usar BCrypt o Argon2 para mayor seguridad
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password; // Fallback (no recomendado en producción)
        }
    }

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
            out.println("<title>Servlet RegisterServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * ========== MÉTODO doGet - MANEJO DE PETICIONES GET ==========
     * 
     * Maneja las peticiones GET redirigiendo al método processRequest.
     * En este caso, el registro solo acepta POST, por lo que GET
     * muestra información básica del servlet.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
        return "RegisterServlet - Maneja el registro de nuevos usuarios aplicando patrones MVC, DAO y DTO";
    }// </editor-fold>

}