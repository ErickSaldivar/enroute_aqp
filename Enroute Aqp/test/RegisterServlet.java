import web.db_crud.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
        
        // Obtener parametros del formulario
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validaciones basicas
        if (username == null || email == null || password == null ||
            username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.sendRedirect("register.jsp?error=empty");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Obtener conexion a la base de datos
            conn = DBConnection.getConnection();
            
            // Hash de la contrasena (mejora la seguridad)
            String hashedPassword = hashPassword(password);
            
            // Insertar usuario en la base de datos
            String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Registro exitoso
                response.sendRedirect("login.jsp?registration=success");
            } else {
                // Error en el registro
                response.sendRedirect("register.jsp?error=database");
            }  
        } catch (Exception e) {
            e.printStackTrace();
            // Verificar si es error de duplicado
            if (e.getMessage().contains("Duplicate")) {
                response.sendRedirect("register.jsp?error=duplicate");
            } else {
                response.sendRedirect("register.jsp?error=unknown");
            }
        } finally {
            // Cerrar recursos
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Metodo simple para hashear contrasenas (en produccion usarias BCrypt)
    private String hashPassword(String password) {
        // En una aplicacion real, usarias BCrypt o similar
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Convertir bytes a hexadecimal
            StringBuilder sb = new StringBuilder();
            for(byte b : hashedBytes) {
                sb.append(String.format("%02%", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // Fallback simple si SHA-256 no esta disponible
            return Integer.toString(password.hashCode());
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
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
