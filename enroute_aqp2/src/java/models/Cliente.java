/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import javax.annotation.ManagedBean;

/**
 *
 * @author User
 */

@ManagedBean
public class Cliente extends User{

    public Cliente() {
    }

    public Cliente(int id, String nombre, String apellido, String email, String password, boolean rol) {
        super(id, nombre, apellido, email, password, rol);
    }
    
}
