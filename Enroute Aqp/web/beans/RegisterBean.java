/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package beans;

/**
 *
 * @author erick
 */

import java.io.Serializable;

public class RegisterBean implements Serializable {
    private String nombre;
    private String apellido;    
    private String email;
    private String password;

    public RegisterBean() {}

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String username) {
        this.nombre = username;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
