/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;
import model.Admin;
import dao.DaoUser;
import model.Pasajero;
import model.User;
import dao.DBConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author aapaz
 */
public class DaoUser_Impl implements DaoUser{
    
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String rol = rs.getString("rol");
        User usuario;

        if ("administrador".equalsIgnoreCase(rol)) {
            usuario = new Admin();
        } else {
            usuario = new Pasajero();
        }

        usuario.setId(rs.getString("id_usuario")); 
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password_hash"));
        usuario.setRol(rol);
        
        return usuario;
    }

    @Override
    public void agregarUsuario(User usuario) throws SQLException {
        
        String sql = "INSERT INTO usuarios(id_usuario, nombre, apellido, email, password, rol, fecha_registro) VALUES(?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getPassword());
            ps.setString(6, usuario.getRol());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificarUsuario(String id, Pasajero pasajero) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ? WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pasajero.getNombre());
            ps.setString(2, pasajero.getApellido());
            ps.setString(3, pasajero.getEmail());
            ps.setString(4, id); 

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public User buscarUsuarioPorId(String id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el usuario por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<User> listarUsuarios() throws SQLException {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    @Override
    public void eliminarUsuario(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id); 
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }


    @Override
    public Admin loginAdmin(String email, String password) throws SQLException {
       
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND rol = 'administrador'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (Admin) mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error en el login de administrador: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Pasajero loginCliente(String email, String password) throws SQLException {
       
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ? AND rol != 'administrador'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (Pasajero) mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error en el login de cliente: " + e.getMessage(), e);
        }
        return null;
    }

}