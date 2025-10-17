/*
 * ========== INTERFACE DAOUSER - PATRÓN DAO PARA USUARIOS ==========
 * 
 * Esta interfaz implementa el patrón DAO (Data Access Object) para la gestión de usuarios:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DAO Pattern: Abstrae el acceso a datos de usuarios
 * 2. Interface Segregation: Define contrato específico para operaciones de usuario
 * 3. Dependency Inversion: Permite diferentes implementaciones de acceso a datos
 * 4. Repository Pattern: Encapsula lógica de acceso a repositorio de usuarios
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Abstracción: Define operaciones sin implementación específica
 * - Polimorfismo: Permite múltiples implementaciones (MySQL, PostgreSQL, etc.)
 * - Separación de responsabilidades: Solo define operaciones de acceso a datos
 * - Inversión de dependencias: El código depende de la abstracción, no de la implementación
 * 
 * OPERACIONES CRUD IMPLEMENTADAS:
 * - Create: agregarUsuario() - Registra nuevos usuarios
 * - Read: buscarUsuarioPorId(), listarUsuarios() - Consulta usuarios
 * - Update: modificarUsuario() - Actualiza datos de usuario
 * - Delete: eliminarUsuario() - Elimina usuarios del sistema
 * 
 * OPERACIONES DE AUTENTICACIÓN:
 * - loginAdmin() - Autenticación específica para administradores
 * - loginCliente() - Autenticación específica para pasajeros
 * 
 * INTEGRACIÓN CON MVC:
 * - Modelo: Define la capa de acceso a datos
 * - Controlador: Usado por servlets para operaciones de usuario
 * - Implementación: DaoUser_Impl proporciona la lógica específica
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package dao;

import model.Admin;
import model.Pasajero;
import model.User;
import java.util.List;

/**
 * DaoUser - Interfaz DAO para operaciones de usuarios
 * 
 * Define el contrato para todas las operaciones de acceso a datos relacionadas
 * con usuarios del sistema EnRoute AQP.
 * 
 * Implementa el patrón DAO para:
 * - Abstraer la lógica de acceso a datos
 * - Permitir diferentes implementaciones de persistencia
 * - Facilitar testing mediante mocks
 * - Mantener separación entre lógica de negocio y acceso a datos
 */
public interface DaoUser {
    // ========== OPERACIONES CRUD - PATRÓN DAO ==========
    
    /**
     * Agrega un nuevo usuario al sistema
     * @param usuario Objeto User (Admin o Pasajero) a registrar
     * @throws Exception Si ocurre error en la operación de inserción
     */
    void agregarUsuario(User usuario) throws Exception;
    
    /**
     * Modifica los datos de un pasajero existente
     * @param id Identificador único del usuario a modificar
     * @param pasajero Objeto Pasajero con los nuevos datos
     * @throws Exception Si ocurre error en la operación de actualización
     */
    void modificarUsuario(String id, Pasajero pasajero) throws Exception;
    
    /**
     * Busca un usuario por su identificador único
     * @param id Identificador del usuario a buscar
     * @return User encontrado o null si no existe
     * @throws Exception Si ocurre error en la consulta
     */
    User buscarUsuarioPorId(String id) throws Exception;
    
    /**
     * Lista todos los usuarios registrados en el sistema
     * @return List<User> con todos los usuarios (Admin y Pasajero)
     * @throws Exception Si ocurre error en la consulta
     */
    List<User> listarUsuarios() throws Exception; 
    
    /**
     * Elimina un usuario del sistema
     * @param id Identificador único del usuario a eliminar
     * @throws Exception Si ocurre error en la operación de eliminación
     */
    void eliminarUsuario(String id) throws Exception;
    
    // ========== OPERACIONES DE AUTENTICACIÓN - PATRÓN STRATEGY ==========
    
    /**
     * Autentica un administrador en el sistema
     * @param email Email del administrador
     * @param password Contraseña encriptada
     * @return Admin autenticado o null si las credenciales son inválidas
     * @throws Exception Si ocurre error en la autenticación
     */
    public Admin loginAdmin(String email, String password) throws Exception;
    
    /**
     * Autentica un pasajero en el sistema
     * @param email Email del pasajero
     * @param password Contraseña encriptada
     * @return Pasajero autenticado o null si las credenciales son inválidas
     * @throws Exception Si ocurre error en la autenticación
     */
    public Pasajero loginCliente(String email, String password) throws Exception;
}
