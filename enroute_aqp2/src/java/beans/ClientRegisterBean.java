package beans;

import dao.DBConnection;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import models.User;
import services.UserApiService;

@ManagedBean(name = "clientRegisterBean")
@RequestScoped
public class ClientRegisterBean implements Serializable {

    private String nombre;
    private String email;
    private String password;
    private String confirmPassword;
    private final UserApiService userApiService;

    public ClientRegisterBean() {
        userApiService = new UserApiService();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    

    public String register() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {

            // Basic validation
            if (nombre == null || nombre.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos incompletos", "Todos los campos son obligatorios."));
                return null;
            }

            if (!password.equals(confirmPassword)) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseñas no coinciden", "La contraseña y su confirmación no coinciden."));
                return null;
            }

            // Check email uniqueness
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE email = ?")) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email ya registrado", "El correo ya está en uso."));
                        return null;
                    }
                }
            }

            // Create user and persist
            User u = new User();
            u.setId(0); // DAO will generate UUID if null
            u.setNombre(nombre);
            u.setApellido("");
            u.setEmail(email);
            u.setPassword(password); // API will hash
            u.setRol(false); // cliente

            userApiService.agregarUsuario(u);

            // After successful registration, authenticate the user automatically
            models.User logged = userApiService.autenticarUsuario(email, password);
            if (logged != null) {
                // Put session values (same keys used in ClientLoginBean)
                fc.getExternalContext().getSessionMap().put("userId", logged.getId());
                fc.getExternalContext().getSessionMap().put("userName", logged.getNombre());
                fc.getExternalContext().getSessionMap().put("userEmail", logged.getEmail());
                fc.getExternalContext().getSessionMap().put("isAdmin", logged.isRol());

                // Add info message and perform a programmatic redirect to avoid
                // any navigation-case resolution issues coming from some JSF impls.
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", "Cuenta creada y autenticada. Bienvenido."));
                try {
                    String ctx = fc.getExternalContext().getRequestContextPath();
                    fc.getExternalContext().redirect(ctx + "/pages/mapa.xhtml");
                } catch (java.io.IOException ioe) {
                    // If redirect fails, fall back to navigation outcome
                    ioe.printStackTrace();
                    return "mapa?faces-redirect=true";
                }
                // We've already redirected
                return null;
            } else {
                // Shouldn't happen, but fallback to explicit login page
                fc.getExternalContext().getFlash().setKeepMessages(true);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", "Cuenta creada correctamente. Por favor inicia sesión."));
                return "loginClient?faces-redirect=true";
            }

        } catch (Exception ex) {
            // Log full stack trace to server logs for diagnosis
            ex.printStackTrace();
            // Show a user-friendly message and ask admin to check server logs
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar", "Ocurrió un error al guardar su cuenta. Por favor contacte al administrador."));
            return null;
        }
    }
}
