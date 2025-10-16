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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Instanciar LoginBean
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(request.getParameter("email"));
        loginBean.setPassword(request.getParameter("password"));
        
        // Validaciones básicas
        if (loginBean.getEmail() == null || loginBean.getPassword() == null || 
            loginBean.getEmail().trim().isEmpty() || loginBean.getPassword().trim().isEmpty()) {
            response.sendRedirect("pages/login.jsp?error=empty");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // Hash de la contraseña para comparar
            String hashedPassword = hashPassword(loginBean.getPassword());
            
            String sql = "SELECT id_usuario, nombre, apellido, es_admin FROM usuarios WHERE email = ? AND password_hash = ?";
            System.out.println("DEBUG LoginServlet - SQL Query: " + sql);
            System.out.println("DEBUG LoginServlet - Email parameter: " + loginBean.getEmail());
            System.out.println("DEBUG LoginServlet - Hashed password: " + hashedPassword);
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginBean.getEmail());
            pstmt.setString(2, hashedPassword);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Login exitoso - Logs de depuración detallados
                System.out.println("DEBUG LoginServlet - Usuario encontrado en BD");
                System.out.println("DEBUG LoginServlet - ID: " + rs.getInt("id_usuario"));
                System.out.println("DEBUG LoginServlet - Nombre: " + rs.getString("nombre"));
                System.out.println("DEBUG LoginServlet - Apellido: " + rs.getString("apellido"));
                
                // Manejo robusto del campo es_admin
                Object esAdminObj = rs.getObject("es_admin");
                boolean isAdmin = false;
                
                System.out.println("DEBUG LoginServlet - Es Admin Object: " + esAdminObj + " (type: " + (esAdminObj != null ? esAdminObj.getClass().getSimpleName() : "null") + ")");
                
                // Estrategia 1: Verificar por email específico (alternativa robusta)
                String userEmail = loginBean.getEmail().toLowerCase();
                if ("jordi@admin.com".equals(userEmail) || "admin@enroute.com".equals(userEmail)) {
                    isAdmin = true;
                    System.out.println("DEBUG LoginServlet - Usuario admin detectado por email: " + userEmail);
                }
                
                // Estrategia 2: Intentar obtener es_admin de la BD si no es null
                if (!isAdmin && esAdminObj != null) {
                    try {
                        // Intentar diferentes tipos de conversión
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
                        isAdmin = "jordi@admin.com".equals(userEmail);
                    }
                }
                
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("id_usuario"));
                session.setAttribute("userName", rs.getString("nombre"));
                session.setAttribute("userLastName", rs.getString("apellido"));
                session.setAttribute("userEmail", loginBean.getEmail());
                session.setAttribute("isAdmin", isAdmin);
                session.setAttribute("loginBean", loginBean);
                
                // Log de depuración final
                System.out.println("DEBUG LoginServlet - Email: " + loginBean.getEmail());
                System.out.println("DEBUG LoginServlet - Valor isAdmin final: " + isAdmin);
                System.out.println("DEBUG LoginServlet - Redirigiendo a: " + (isAdmin ? "dashboard.jsp" : "mapa.jsp"));
                
                if (isAdmin) {
                    System.out.println("DEBUG - Redirigiendo a dashboard.jsp");
                    response.sendRedirect("pages/dashboard.jsp");
                } else {
                    System.out.println("DEBUG - Redirigiendo a mapa.jsp");
                    response.sendRedirect("pages/mapa.jsp");
                }
            } else {
                // Credenciales incorrectas
                response.sendRedirect("pages/login.jsp?error=invalid");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("pages/login.jsp?error=database");
        } finally {
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