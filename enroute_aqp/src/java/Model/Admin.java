/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author aapaz
 */
public class Admin extends User{
    private String telefono;
    private String direccion;

    public Admin() {
    }

    
    
    public Admin(String telefono, String direccion) {
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Admin(String telefono, String direccion, String id, String nombre, String apellido, String email, String password, String rol) {
        super(id, nombre, apellido, email, password, rol);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    

}
