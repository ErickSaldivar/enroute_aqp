/*
 * ========== CLASE LOGINBEAN - PATRÓN DTO PARA AUTENTICACIÓN ==========
 * 
 * Esta clase implementa el patrón DTO (Data Transfer Object) para el proceso de login:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DTO Pattern: Encapsula datos de transferencia entre capas
 * 2. JavaBean Pattern: Implementa getters/setters estándar
 * 3. Value Object Pattern: Objeto inmutable para transferir datos
 * 4. Serializable Pattern: Permite persistencia y transferencia
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Encapsulación: Atributos privados con acceso controlado
 * - Serialización: Implementa Serializable para sesiones HTTP
 * - Separación de responsabilidades: Solo maneja datos de login
 * - Validación de entrada: Estructura para validar credenciales
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Captura de credenciales de usuario (email/password)
 * - Transferencia segura de datos entre vista y controlador
 * - Validación de formato de datos de entrada
 * - Soporte para autenticación en el sistema
 * 
 * INTEGRACIÓN CON MVC:
 * - Vista: Recibe datos del formulario login.jsp
 * - Controlador: Usado por LoginServlet para procesar autenticación
 * - Modelo: Interfaz con las clases User/Admin/Pasajero
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package model;

import java.io.Serializable;

/**
 * LoginBean - DTO para el proceso de autenticación
 * 
 * Encapsula las credenciales del usuario para el proceso de login.
 * Implementa Serializable para permitir su almacenamiento en sesiones HTTP.
 * 
 * Este bean actúa como intermediario entre:
 * - El formulario HTML de login (vista)
 * - El LoginServlet (controlador)
 * - La base de datos de usuarios (modelo)
 */
public class LoginBean implements Serializable {
    // ========== ATRIBUTOS DTO - CREDENCIALES DE AUTENTICACIÓN ==========
    private String email;      // Email del usuario para autenticación
    private String password;   // Contraseña del usuario (se encriptará)

    // ========== CONSTRUCTOR ==========
    /**
     * Constructor por defecto - Patrón JavaBean
     * Requerido para frameworks que usan reflexión (JSP, Servlets)
     */
    public LoginBean() {}

    // ========== MÉTODOS GETTER/SETTER - PATRÓN JAVABEAN ==========
    /**
     * Obtiene el email del usuario
     * @return String con el email para autenticación
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email del usuario
     * @param email Email válido para autenticación
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario
     * @return String con la contraseña (texto plano desde formulario)
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Establece la contraseña del usuario
     * @param password Contraseña en texto plano (se encriptará posteriormente)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}