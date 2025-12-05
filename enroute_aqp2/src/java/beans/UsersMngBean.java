package beans;

import dao.DaoUser;
import dao.impl.DaoUser_Impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import models.User;

@ManagedBean(name = "usersMngBean")
@ViewScoped
public class UsersMngBean implements Serializable {

    private List<User> users;
    private int selectedUserId;
    private String superAdminPassword;

    public UsersMngBean() {
        users = new ArrayList<>();
        // cargar usuarios en el constructor para evitar dependencias de javax.annotation
        DaoUser dao = new DaoUser_Impl();
        try {
            users = dao.listarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public int getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(int selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public void setSuperAdminPassword(String superAdminPassword) {
        this.superAdminPassword = superAdminPassword;
    }

    public String rolTexto(User u) {
        return u.isRol() ? "Admin" : "Cliente";
    }
    
    public String promoteToAdmin(int userId, String password) {
        // Validar contraseña de superadmin
        if (!"unodostres_123".equals(password)) {
            return "error";
        }
        
        // Promover usuario en la base de datos
        DaoUser dao = new DaoUser_Impl();
        try {
            dao.promoteToAdmin(userId);
            // Actualizar la lista local
            for (User u : users) {
                if (u.getId() == userId) {
                    u.setRol(true);
                    break;
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    public String demoteToClient(int userId, String password) {
        // Validar contraseña de superadmin
        if (!"unodostres_123".equals(password)) {
            return "error";
        }
        
        // Convertir admin a cliente en la base de datos
        DaoUser dao = new DaoUser_Impl();
        try {
            dao.demoteToClient(userId);
            // Actualizar la lista local
            for (User u : users) {
                if (u.getId() == userId) {
                    u.setRol(false);
                    break;
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
