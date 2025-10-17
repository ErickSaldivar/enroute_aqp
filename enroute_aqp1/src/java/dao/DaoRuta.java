/*
 * ========== INTERFACE DAORUTA - PATRÓN DAO PARA RUTAS ==========
 * 
 * Esta interfaz implementa el patrón DAO (Data Access Object) para la gestión de rutas:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DAO Pattern: Abstrae el acceso a datos de rutas del sistema de transporte
 * 2. Interface Segregation: Define contrato específico para operaciones de rutas
 * 3. Dependency Inversion: Permite diferentes implementaciones de persistencia
 * 4. Repository Pattern: Encapsula lógica de acceso a repositorio de rutas
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Abstracción: Define operaciones sin implementación específica
 * - Polimorfismo: Permite múltiples implementaciones de base de datos
 * - Separación de responsabilidades: Solo operaciones de acceso a datos de rutas
 * - Inversión de dependencias: Código depende de abstracción, no implementación
 * 
 * OPERACIONES CRUD IMPLEMENTADAS:
 * - Create: registrar() - Registra nuevas rutas en el sistema
 * - Read: listarParadas() - Lista todas las rutas con sus paradas
 * - Update: modificar() - Actualiza información de rutas existentes
 * - Delete: eliminar() - Elimina rutas del sistema
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Gestión de rutas de transporte público
 * - Definición de recorridos y trayectorias
 * - Asignación de paradas a rutas específicas
 * - Control de horarios y frecuencias de rutas
 * 
 * INTEGRACIÓN CON MVC:
 * - Modelo: Define la capa de acceso a datos de rutas
 * - Controlador: Usado por servlets de administración de rutas
 * - Implementación: DaoRuta_Impl proporciona la lógica específica de BD
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package dao;

import model.Ruta;
import java.util.List;

/**
 * DaoRuta - Interfaz DAO para operaciones de rutas
 * 
 * Define el contrato para todas las operaciones de acceso a datos relacionadas
 * con la gestión de rutas del sistema de transporte público EnRoute AQP.
 * 
 * Implementa el patrón DAO para:
 * - Abstraer la lógica de acceso a datos de rutas
 * - Permitir diferentes implementaciones de persistencia
 * - Facilitar testing mediante mocks
 * - Mantener separación entre lógica de negocio y acceso a datos
 * - Gestionar el sistema de rutas y recorridos
 */
public interface DaoRuta {
    
    // ========== OPERACIONES CRUD - PATRÓN DAO ==========
    
    /**
     * Registra una nueva ruta en el sistema
     * @param ruta Objeto Ruta con la información del recorrido a registrar
     * @throws Exception Si ocurre error en la operación de inserción
     */
    public void registrar(Ruta ruta) throws Exception;
    
    /**
     * Modifica la información de una ruta existente
     * @param id Identificador único de la ruta a modificar
     * @param ruta Objeto Ruta con los nuevos datos
     * @throws Exception Si ocurre error en la operación de actualización
     */
    public void modificar(int id, Ruta ruta) throws Exception;
    
    /**
     * Elimina una ruta del sistema
     * @param id Identificador único de la ruta a eliminar
     * @throws Exception Si ocurre error en la operación de eliminación
     */
    public void eliminar(int id) throws Exception;
    
    /**
     * Lista todas las rutas con sus paradas asociadas
     * @return List<Ruta> con todas las rutas del sistema
     * @throws Exception Si ocurre error en la consulta
     */
    public List<Ruta> listarParadas() throws Exception;
}
