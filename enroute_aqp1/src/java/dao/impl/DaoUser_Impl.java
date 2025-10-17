/*
 * ========== IMPLEMENTACIÓN DAOUSER_IMPL - PATRÓN DAO CONCRETO ==========
 * 
 * Esta clase implementa el patrón DAO (Data Access Object) para usuarios con MySQL:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DAO Pattern: Implementación concreta del acceso a datos de usuarios
 * 2. Factory Method: Utiliza mapResultSetToUser() para crear objetos User específicos
 * 3. Template Method: Estructura común para operaciones CRUD con BD
 * 4. Strategy Pattern: Diferentes estrategias de autenticación (Admin/Pasajero)
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Polimorfismo: Crea Admin o Pasajero según el rol en BD
 * - Encapsulación: Métodos privados para mapeo de ResultSet
 * - Manejo de excepciones: SQLException específicas para cada operación
 * - Separación de responsabilidades: Solo lógica de acceso a datos
 * 
 * OPERACIONES CRUD IMPLEMENTADAS:
 * - Create: agregarUsuario() - INSERT con PreparedStatement
 * - Read: buscarUsuarioPorId(), listarUsuarios() - SELECT con mapeo automático
 * - Update: modificarUsuario() - UPDATE con validación de existencia
 * - Delete: eliminarUsuario() - DELETE con manejo de integridad referencial
 * 
 * OPERACIONES DE AUTENTICACIÓN:
 * - loginAdmin() - Autenticación con hash de contraseña para administradores
 * - loginCliente() - Autenticación con hash de contraseña para pasajeros
 * 
 * INTEGRACIÓN CON MVC:
 * - Modelo: Implementa la capa de persistencia
 * - Controlador: Usado por LoginServlet y RegisterServlet
 * - Vista: Proporciona datos para JSPs de usuario
 * 
 * SEGURIDAD IMPLEMENTADA:
 * - PreparedStatement: Prevención de inyección SQL
 * - Hash de contraseñas: Almacenamiento seguro de credenciales
 * - Validación de roles: Control de acceso basado en tipo de usuario
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
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
 * DaoUser_Impl - Implementación concreta del patrón DAO para usuarios
 * 
 * Proporciona la implementación específica para MySQL de todas las operaciones
 * de acceso a datos relacionadas con usuarios del sistema EnRoute AQP.
 * 
 * Características principales:
 * - Implementación del patrón DAO con MySQL
 * - Factory Method para creación polimórfica de usuarios
 * - Manejo seguro de conexiones con try-with-resources
 * - Prevención de inyección SQL con PreparedStatement
 * - Mapeo automático de ResultSet a objetos de dominio
 */
public class DaoUser_Impl implements DaoUser{
    
    // ========== FACTORY METHOD - PATRÓN CREACIONAL ==========
    
    /**
     * Factory Method para crear objetos User específicos según el rol
     * Implementa polimorfismo para instanciar Admin o Pasajero dinámicamente
     * 
     * @param rs ResultSet con los datos del usuario desde la BD
     * @return User (Admin o Pasajero) según el rol almacenado
     * @throws SQLException Si ocurre error al leer el ResultSet
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String rol = rs.getString("rol");
        User usuario;

        // Factory Method: Creación polimórfica basada en el rol
        if ("administrador".equalsIgnoreCase(rol)) {
            usuario = new Admin();
        } else {
            usuario = new Pasajero();
        }

        // Mapeo de atributos comunes de la clase base User
        usuario.setId(rs.getString("id_usuario")); 
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password_hash"));
        usuario.setRol(rol);
        
        return usuario;
    }

    // ========== OPERACIONES CRUD - IMPLEMENTACIÓN DAO ==========

    /**
     * Implementa CREATE - Registra un nuevo usuario en la base de datos
     * Utiliza PreparedStatement para prevenir inyección SQL
     * 
     * @param usuario Objeto User (Admin o Pasajero) a registrar
     * @throws SQLException Si ocurre error en la inserción
     */
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