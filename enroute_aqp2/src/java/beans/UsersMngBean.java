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

    public String rolTexto(User u) {
        return u.isRol() ? "Admin" : "Cliente";
    }
}
