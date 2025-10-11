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


@WebServlet(name= "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
        
        // Instanciar RegisterBean
        RegisterBean registerBean = new RegisterBean();
        registerBean.setNombre(request.getParameter("nombre"));
        registerBean.setApellido(request.getParameter("apellido"));
        registerBean.setEmail(request.getParameter("email"));
        registerBean.setPassword(request.getParameter("password"));
        String confirmPassword = request.getParameter("confirm-password");
       
        
        // Validaciones basicas
        if (registerBean.getNombre() == null || registerBean.getApellido() == null || 
            registerBean.getEmail() == null || registerBean.getPassword() == null ||
            registerBean.getNombre().isEmpty() || registerBean.getApellido().isEmpty() || 
            registerBean.getEmail().isEmpty() || registerBean.getPassword().isEmpty()) {
            response.sendRedirect("pages/register.jsp?error=empty");
            return;
        }
        
        if (!registerBean.getPassword().equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            RequestDispatcher rd = request.getRequestDispatcher("pages/register.jsp");
            rd.forward(request, response);
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Obtener conexion a la base de datos
            conn = DBConnection.getConnection();
            
            // Hashear la contraseña
            String hashedPassword = hashPassword(registerBean.getPassword());
            
            // Insertar usuario en la base de datos
            String sql = "INSERT INTO usuarios (nombre, apellido, email, password, fecha_registro) VALUES(?, ?, ?, ?, NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, registerBean.getNombre());
            pstmt.setString(2, registerBean.getApellido());
            pstmt.setString(3, registerBean.getEmail());
            pstmt.setString(4, hashedPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Registro exitoso
                response.sendRedirect("pages/login.jsp?registration=success");
            } else {
                // Error en el registro
                response.sendRedirect("pages/register.jsp?error=database");
            }  
        } catch (Exception e) {
            e.printStackTrace();
            // Verificar si es error de duplicado
            if (e.getMessage().contains("Duplicate")) {
                response.sendRedirect("pages/register.jsp?error=duplicate");
            } else {
                response.sendRedirect("pages/register.jsp?error=unknown");
            }
        } finally {
            // Cerrar recursos
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Metodo simple para hashear la contraseña
     * En produccion se deberia usar BCrypt o similar
     */
    private String hashPassword(String password) {
        // Por simplicidad, usamos un hash basico
        // En produccion usar BCrypt
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}