/*
 * ========== CLASE ADMIN - ESPECIALIZACIÓN DE USER ==========
 * 
 * Esta clase implementa múltiples patrones de diseño para administradores del sistema:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. Inheritance Pattern: Extiende la clase abstracta User
 * 2. Specialization Pattern: Añade atributos específicos de administradores
 * 3. DTO Pattern: Encapsula datos específicos de administradores
 * 4. JavaBean Pattern: Implementa getters/setters estándar
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Herencia: Reutiliza funcionalidad de la clase User
 * - Polimorfismo: Puede ser tratado como User en contextos generales
 * - Encapsulación: Atributos privados con acceso controlado
 * - Especialización: Añade campos específicos para administradores
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Gestión de administradores del sistema de transporte
 * - Almacenamiento de información de contacto adicional
 * - Control de acceso privilegiado al dashboard administrativo
 * - Gestión de rutas, buses y paradas del sistema
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package model;

/**
 * Clase Admin - Especialización de User para administradores
 * 
 * Extiende User añadiendo campos específicos para administradores:
 * - Información de contacto adicional (teléfono, dirección)
 * - Acceso a funcionalidades administrativas del sistema
 * 
 * Implementa el patrón de herencia para reutilizar la funcionalidad base
 * mientras añade características específicas del rol administrativo.
 */
public class Admin extends User{
    // ========== ATRIBUTOS ESPECÍFICOS DE ADMINISTRADOR ==========
    // Campos adicionales que extienden la funcionalidad base de User
    private String telefono;   // Teléfono de contacto del administrador
    private String direccion;  // Dirección física del administrador

    // ========== CONSTRUCTORES ==========
    /**
     * Constructor por defecto - Patrón JavaBean
     * Requerido para frameworks que usan reflexión
     */
    public Admin() {
    }

    /**
     * Constructor parcial - Solo campos específicos de Admin
     * @param telefono Teléfono de contacto
     * @param direccion Dirección física
     */
    public Admin(String telefono, String direccion) {
        this.telefono = telefono;
        this.direccion = direccion;
    }

    /**
     * Constructor completo - Combina campos de User y Admin
     * Utiliza super() para inicializar la clase padre
     * 
     * @param telefono Teléfono de contacto del admin
     * @param direccion Dirección física del admin
     * @param id Identificador único heredado de User
     * @param nombre Nombre heredado de User
     * @param apellido Apellido heredado de User
     * @param email Email heredado de User
     * @param password Contraseña heredada de User
     * @param rol Rol heredado de User (debe ser "administrador")
     */
    public Admin(String telefono, String direccion, String id, String nombre, String apellido, String email, String password, String rol) {
        super(id, nombre, apellido, email, password, rol);  // Inicializa campos de la clase padre
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // ========== MÉTODOS GETTER/SETTER - PATRÓN JAVABEAN ==========
    /**
     * Obtiene el teléfono del administrador
     * @return String con el número de teléfono
     */
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
