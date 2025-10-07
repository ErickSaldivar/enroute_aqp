/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.Interfaces;

import Model.Admin;
import Model.Pasajero;
import Model.User;
import java.util.List;

/**
 *
 * @author aapaz
 */
public interface DaoUser {
    void agregarUsuario(User usuario) throws Exception;
    void modificarUsuario(String id, Pasajero pasajero) throws Exception;
    User buscarUsuarioPorId(String id) throws Exception;
    List<User> listarUsuarios() throws Exception; 
    void eliminarUsuario(String id) throws Exception;
    public Admin loginAdmin(String email, String password) throws Exception;
    public Pasajero loginCliente(String email, String password) throws Exception;
}
