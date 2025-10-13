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
            
            String sql = "SELECT id_usuario, nombre, apellido FROM usuarios WHERE email = ? AND password_hash = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginBean.getEmail());
            pstmt.setString(2, hashedPassword);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Login exitoso
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("id_usuario"));
                session.setAttribute("userName", rs.getString("nombre"));
                session.setAttribute("userLastName", rs.getString("apellido"));
                session.setAttribute("userEmail", loginBean.getEmail());
                session.setAttribute("loginBean", loginBean);
                
                response.sendRedirect("pages/mapa.jsp");
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