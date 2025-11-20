package beans;

import dao.DaoUser;
import dao.impl.DaoUser_Impl;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.User;

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

    public UserBean() {
        // initialize from session if possible
        Object uid = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        if (uid != null) {
            try {
                int userId = (Integer) uid;
                DaoUser dao = new DaoUser_Impl();
                User u = dao.obtenerUsuarioPorId(userId);
                if (u != null) {
                    this.user = u;
                    this.id = u.getId();
                    this.nombre = u.getNombre();
                    this.apellido = u.getApellido();
                    this.email = u.getEmail();
                    this.fechaRegistro = u.getFechaRegistro();
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
            // Si el usuario escribió un nuevo valor en los inputs, lo usamos; si no, conservamos el existente
            String finalNombre = (this.nombreInput != null && !this.nombreInput.trim().isEmpty()) ? this.nombreInput.trim() : this.user.getNombre();
            String finalApellido = (this.apellidoInput != null && !this.apellidoInput.trim().isEmpty()) ? this.apellidoInput.trim() : this.user.getApellido();

            this.user.setNombre(finalNombre);
            this.user.setApellido(finalApellido);
            // Do not change email or fechaRegistro here
            this.user.setPassword(null); // ensure password not updated

            DaoUser dao = new DaoUser_Impl();
            dao.modificarUsuario(this.user);

            // Update session name
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", this.nombre);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", this.user.getNombre());

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
            }
            // set plain password - Dao will hash it
            this.user.setPassword(this.newPassword);
            DaoUser dao = new DaoUser_Impl();
            dao.modificarUsuario(this.user);

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
