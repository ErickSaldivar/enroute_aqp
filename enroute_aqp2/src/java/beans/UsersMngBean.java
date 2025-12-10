package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import models.User;
import services.UserApiService;

@ManagedBean(name = "usersMngBean")
@ViewScoped
public class UsersMngBean implements Serializable {

    private List<User> users;
    private int selectedUserId;
    private String superAdminPassword;
    private final UserApiService userApiService;

    public UsersMngBean() {
        users = new ArrayList<>();
        userApiService = new UserApiService();
        
        // Cargar usuarios desde el API REST
        try {
            users = userApiService.listarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar usuarios desde API: " + e.getMessage());
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
    
    public String promoteToAdmin() {
        System.out.println("=== promoteToAdmin llamado ===");
        System.out.println("selectedUserId: " + selectedUserId);
        System.out.println("superAdminPassword: " + superAdminPassword);
        
        // Validar contraseña de superadmin
        if (!"unodostres_123".equals(superAdminPassword)) {
            System.err.println("Contraseña incorrecta: " + superAdminPassword);
            return "error";
        }
        
        // Promover usuario mediante el API REST
        try {
            System.out.println("Llamando a API para promover usuario " + selectedUserId);
            userApiService.promoteToAdmin(selectedUserId);
            
            // Actualizar la lista local
            for (User u : users) {
                if (u.getId() == selectedUserId) {
                    u.setRol(true);
                    System.out.println("Usuario " + u.getNombre() + " actualizado localmente a admin");
                    break;
                }
            }
            System.out.println("Usuario " + selectedUserId + " promovido a admin exitosamente");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al promover usuario: " + e.getMessage());
            return "error";
        }
    }
    
    public String demoteToClient() {
        System.out.println("=== demoteToClient llamado ===");
        System.out.println("selectedUserId: " + selectedUserId);
        System.out.println("superAdminPassword: " + superAdminPassword);
        
        // Validar contraseña de superadmin
        if (!"unodostres_123".equals(superAdminPassword)) {
            System.err.println("Contraseña incorrecta: " + superAdminPassword);
            return "error";
        }
        
        // Convertir admin a cliente mediante el API REST
        try {
            System.out.println("Llamando a API para convertir usuario " + selectedUserId);
            userApiService.demoteToClient(selectedUserId);
            
            // Actualizar la lista local
            for (User u : users) {
                if (u.getId() == selectedUserId) {
                    u.setRol(false);
                    System.out.println("Usuario " + u.getNombre() + " actualizado localmente a cliente");
                    break;
                }
            }
            System.out.println("Usuario " + selectedUserId + " convertido a cliente exitosamente");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al convertir usuario a cliente: " + e.getMessage());
            return "error";
        }
    }
}
