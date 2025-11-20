package beans;

import dao.DaoUser;
import dao.impl.DaoUser_Impl;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import models.User;

@ManagedBean(name = "clientLoginBean")
@SessionScoped
public class ClientLoginBean implements Serializable {

    private String email;
    private String password;
    private User user;

    public ClientLoginBean() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String login() {
        DaoUser dao = new DaoUser_Impl();
        try {
            User u = dao.autenticarUsuario(email, password);
            if (u != null) {
                this.user = u;
                // store user info and role in session map so views can render accordingly
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.getExternalContext().getSessionMap().put("userId", u.getId());
                ctx.getExternalContext().getSessionMap().put("userName", u.getNombre());
                ctx.getExternalContext().getSessionMap().put("userEmail", u.getEmail());
                ctx.getExternalContext().getSessionMap().put("isAdmin", u.isRol());
                ctx.getExternalContext().getSessionMap().put("userRole", u.isRol() ? "admin" : "client");
                // Redirect all authenticated users to the main map page; sidebar will show admin links when isAdmin=true
                return "mapa";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", "Correo o contraseña incorrectos."));
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de servidor", "Ocurrió un error al procesar el inicio de sesión. Contacta al administrador."));
            return null;
        }
    }
}
