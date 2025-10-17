/*
 * ========== CLASE USER - MODELO BASE ABSTRACTO ==========
 * 
 * Esta clase implementa múltiples patrones de diseño para el sistema EnRoute AQP:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. Template Method Pattern: Define la estructura base para todos los usuarios
 * 2. Strategy Pattern: Permite diferentes implementaciones según el tipo de usuario
 * 3. DTO Pattern: Encapsula datos de transferencia entre capas
 * 4. JavaBean Pattern: Implementa getters/setters estándar
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Herencia: Clase abstracta que define la estructura común
 * - Polimorfismo: Permite tratar Admin y Pasajero de forma uniforme
 * - Encapsulación: Atributos protegidos con acceso controlado
 * - Serialización: Implementa Serializable para persistencia y sesiones
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Gestión de usuarios del sistema de transporte
 * - Control de acceso basado en roles (admin/pasajero)
 * - Almacenamiento de información personal básica
 * - Soporte para autenticación y autorización
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package model;
import java.io.Serializable;

/**
 * Clase abstracta User - Implementa el patrón Template Method
 * 
 * Define la estructura base para todos los tipos de usuario en el sistema.
 * Las clases hijas (Admin, Pasajero) extienden esta funcionalidad base.
 * 
 * Implementa Serializable para:
 * - Almacenamiento en sesiones HTTP
 * - Transferencia entre capas del sistema
 * - Persistencia temporal de objetos
 */
public abstract class User implements Serializable{
    // ========== ATRIBUTOS PROTEGIDOS - PATRÓN TEMPLATE METHOD ==========
    // Atributos comunes a todos los tipos de usuario
    // Uso de 'protected' permite acceso desde clases hijas (Admin, Pasajero)
    protected String id;        // Identificador único del usuario
    protected String nombre;    // Nombre del usuario
    protected String apellido;  // Apellido del usuario  
    protected String email;     // Email único para autenticación
    protected String password;  // Contraseña encriptada
    protected String rol;       // Rol del usuario (admin/pasajero)

    // ========== CONSTRUCTORES ==========
    /**
     * Constructor por defecto - Patrón JavaBean
     * Requerido para frameworks que usan reflexión (JSP, Servlets)
     */
    public User() {
    }

    /**
     * Constructor completo - Patrón Builder implícito
     * Inicializa todos los atributos del usuario
     * 
     * @param id Identificador único
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param email Email único para login
     * @param password Contraseña (debe estar encriptada)
     * @param rol Rol del usuario en el sistema
     */
    public User(String id, String nombre, String apellido, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // ========== MÉTODOS GETTER/SETTER - PATRÓN JAVABEAN ==========
    /**
     * Obtiene el ID único del usuario
     * @return String con el identificador
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del usuario
     * @param id Nuevo identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario
     * @return String con el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario
     * @return String con el apellido
     */
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
      
}
