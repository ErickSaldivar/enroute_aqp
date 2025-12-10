package beans;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.User;
import services.UserApiService;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaRegistro;

    private String newPassword;
    private String confirmPassword;
    private String nombreInput;
    private String apellidoInput;

    private User user;
    private final UserApiService userApiService;

    public UserBean() {
        userApiService = new UserApiService();
        // initialize from session if possible
        Object uid = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        if (uid != null) {
            try {
                int userId = (Integer) uid;
                User u = userApiService.obtenerUsuarioPorId(userId);
                if (u != null) {
                    this.user = u;
                    this.id = u.getId();
                    this.nombre = u.getNombre();
                    this.apellido = u.getApellido();
                    this.email = u.getEmail();
                    this.fechaRegistro = u.getFechaRegistro();
                    // Inicializar campos de entrada con valores actuales
                    this.nombreInput = u.getNombre();
                    this.apellidoInput = u.getApellido();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // try email in session
            Object emailObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userEmail");
            if (emailObj != null) {
                this.email = (String) emailObj;
            }
        }
    }

    

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNombreInput() {
        return nombreInput;
    }

    public void setNombreInput(String nombreInput) {
        this.nombreInput = nombreInput;
    }

    public String getApellidoInput() {
        return apellidoInput;
    }

    public void setApellidoInput(String apellidoInput) {
        this.apellidoInput = apellidoInput;
    }

    public String updateProfile() {
        try {
            if (this.user == null) {
                this.user = new User();
                this.user.setId(this.id);
            }
            
            // Actualizar con los valores de los inputs
            this.user.setNombre(this.nombreInput != null ? this.nombreInput.trim() : this.nombre);
            this.user.setApellido(this.apellidoInput != null ? this.apellidoInput.trim() : this.apellido);
            this.user.setEmail(this.email);
            this.user.setPassword(null); // no actualizar contraseña aquí

            userApiService.modificarUsuario(this.user);

            // Actualizar variables locales y sesión
            this.nombre = this.user.getNombre();
            this.apellido = this.user.getApellido();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", this.nombre);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Perfil actualizado", "Tus datos se han guardado correctamente."));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el perfil."));
            return null;
        }
    }

    public String changePassword() {
        try {
            if (newPassword == null || newPassword.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contraseña vacía", "Ingresa una nueva contraseña."));
                return null;
            }
            if (!newPassword.equals(confirmPassword)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No coinciden", "Las contraseñas no coinciden."));
                return null;
            }
            if (this.user == null) {
                this.user = new User();
                this.user.setId(this.id);
                this.user.setNombre(this.nombre);
                this.user.setApellido(this.apellido);
                this.user.setEmail(this.email);
            }
            // set plain password - API will hash it
            this.user.setPassword(this.newPassword);
            userApiService.modificarUsuario(this.user);

            // clear fields
            this.newPassword = null;
            this.confirmPassword = null;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contraseña cambiada", "Tu contraseña se actualizó correctamente."));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cambiar la contraseña."));
            return null;
        }
    }

}
