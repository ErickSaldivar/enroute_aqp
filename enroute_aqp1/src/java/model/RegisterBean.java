/*
 * ========== CLASE REGISTERBEAN - PATRÓN DTO PARA REGISTRO ==========
 * 
 * Esta clase implementa el patrón DTO (Data Transfer Object) para el proceso de registro:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DTO Pattern: Encapsula datos de transferencia entre capas
 * 2. JavaBean Pattern: Implementa getters/setters estándar
 * 3. Value Object Pattern: Objeto para transferir datos de registro
 * 4. Serializable Pattern: Permite persistencia y transferencia
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Encapsulación: Atributos privados con acceso controlado
 * - Serialización: Implementa Serializable para sesiones HTTP
 * - Separación de responsabilidades: Solo maneja datos de registro
 * - Validación de entrada: Estructura para validar datos de nuevo usuario
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Captura de datos de nuevo usuario (nombre, apellido, email, password)
 * - Transferencia segura de datos entre vista y controlador
 * - Validación de formato de datos de entrada
 * - Soporte para registro de nuevos usuarios en el sistema
 * 
 * INTEGRACIÓN CON MVC:
 * - Vista: Recibe datos del formulario register.jsp
 * - Controlador: Usado por RegisterServlet para procesar registro
 * - Modelo: Se convierte en objeto User/Pasajero para persistencia
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package model;

import java.io.Serializable;

/**
 * RegisterBean - DTO para el proceso de registro de usuarios
 * 
 * Encapsula los datos del nuevo usuario para el proceso de registro.
 * Implementa Serializable para permitir su almacenamiento en sesiones HTTP.
 * 
 * Este bean actúa como intermediario entre:
 * - El formulario HTML de registro (vista)
 * - El RegisterServlet (controlador)
 * - La base de datos de usuarios (modelo)
 */
public class RegisterBean implements Serializable {
    private String nombre;
    private String apellido;    
    private String email;
    private String password;

    public RegisterBean() {}

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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
}
