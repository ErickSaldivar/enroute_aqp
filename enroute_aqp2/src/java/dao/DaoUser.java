/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.User;

import java.util.List;

/**
 *
 * @author Erick
 */
public interface DaoUser {
    
    void agregarUsuario(User user) throws Exception;

    void modificarUsuario(User user) throws Exception;

    User obtenerUsuarioPorId(int id) throws Exception;

    List<User> listarUsuarios() throws Exception;

    void eliminarUsuario(int id) throws Exception;

    public User autenticarUsuario(String username, String password) throws Exception;
    
    void promoteToAdmin(int userId) throws Exception;
    
    void demoteToClient(int userId) throws Exception;

}
