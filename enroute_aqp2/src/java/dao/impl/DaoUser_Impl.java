/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.DBConnection;
import dao.DaoUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import models.User;

/**
 *
 * @author User
 */
public class DaoUser_Impl implements DaoUser {

    @Override
    public void agregarUsuario(User user) throws Exception {
        String sql = "INSERT INTO usuarios(id_usuario, nombre, apellido, email, password_hash, es_admin, fecha_registro) VALUES(?, ?, ?, ?, ?, ?, NOW())";
        // Asegurarse de tener un id; si no, generar uno
        //user.setId(java.util.UUID.randomUUID().toString());
        

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, 0);
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getApellido());
            ps.setString(4, user.getEmail());

            // Hash de la contraseña aquí
            String plain = user.getPassword();
            String hashed = (plain == null) ? null : hashPassword(plain);
            ps.setString(5, hashed);

            // En el modelo `User` el rol es boolean (isRol()), donde true puede representar administrador
            ps.setBoolean(6, user.isRol());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificarUsuario(User user) throws Exception {
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET nombre = ?, apellido = ?");
        boolean updatePassword = user.getPassword() != null && !user.getPassword().isEmpty();
        if (updatePassword) {
            sql.append(", password_hash = ?");
        }
        sql.append(" WHERE id_usuario = ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            int idx = 3;
            if (updatePassword) {
                ps.setString(idx++, hashPassword(user.getPassword()));
            }
            ps.setInt(idx, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public User obtenerUsuarioPorId(int id) throws Exception {
        String sql = "SELECT id_usuario, nombre, apellido, email, password_hash, es_admin, fecha_registro FROM usuarios WHERE id_usuario = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password_hash"));
                    u.setRol(rs.getBoolean("es_admin"));
                    u.setFechaRegistro(rs.getString("fecha_registro"));
                    return u;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error obteniendo usuario por id: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<User> listarUsuarios() throws Exception {
        String sql = "SELECT id_usuario, nombre, apellido, email, password_hash, es_admin, fecha_registro FROM usuarios ORDER BY id_usuario";
        java.util.List<User> lista = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password_hash"));
                u.setRol(rs.getBoolean("es_admin"));
                u.setFechaRegistro(rs.getString("fecha_registro"));
                lista.add(u);
            }
            return lista;
        } catch (SQLException e) {
            throw new SQLException("Error listando usuarios: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarUsuario(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public void promoteToAdmin(int userId) throws Exception {
        String sql = "UPDATE usuarios SET es_admin = true WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al promover usuario a admin: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void demoteToClient(int userId) throws Exception {
        String sql = "UPDATE usuarios SET es_admin = false WHERE id_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al convertir admin a cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public User autenticarUsuario(String username, String password) throws Exception {
        String sql = "SELECT id_usuario, nombre, apellido, email, password_hash, es_admin, fecha_registro FROM usuarios WHERE email = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String providedHash = (password == null) ? null : hashPassword(password);
                    if (storedHash != null && storedHash.equals(providedHash)) {
                        User u = new User();
                        u.setId(rs.getInt("id_usuario"));
                        u.setNombre(rs.getString("nombre"));
                        u.setApellido(rs.getString("apellido"));
                        u.setEmail(rs.getString("email"));
                        u.setPassword(storedHash);
                        u.setRol(rs.getBoolean("es_admin"));
                        return u;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error autenticando usuario: " + e.getMessage(), e);
        }
        return null;
    }

    private String hashPassword(String password) {
        // Por simplicidad, usamos un hash básico SHA-256
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password; // Fallback (no recomendado en producción)
        }
    }
    
}
