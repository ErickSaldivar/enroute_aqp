/*
 * ========== INTERFACE DAOBUS - PATRÓN DAO PARA BUSES ==========
 * 
 * Esta interfaz implementa el patrón DAO (Data Access Object) para la gestión de buses:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DAO Pattern: Abstrae el acceso a datos de buses del sistema de transporte
 * 2. Interface Segregation: Define contrato específico para operaciones de buses
 * 3. Dependency Inversion: Permite diferentes implementaciones de persistencia
 * 4. Repository Pattern: Encapsula lógica de acceso a repositorio de buses
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Abstracción: Define operaciones sin implementación específica
 * - Polimorfismo: Permite múltiples implementaciones de base de datos
 * - Separación de responsabilidades: Solo operaciones de acceso a datos de buses
 * - Inversión de dependencias: Código depende de abstracción, no implementación
 * 
 * OPERACIONES CRUD IMPLEMENTADAS:
 * - Create: registrar() - Registra nuevos buses en el sistema
 * - Read: findByRuta() - Consulta buses por ruta específica
 * - Update: modificar() - Actualiza información de buses existentes
 * - Delete: eliminar() - Elimina buses del sistema
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Gestión de flota de buses por empresa de transporte
 * - Asignación de buses a rutas específicas
 * - Control de disponibilidad y estado de buses
 * - Seguimiento de buses en tiempo real
 * 
 * INTEGRACIÓN CON MVC:
 * - Modelo: Define la capa de acceso a datos de buses
 * - Controlador: Usado por servlets de administración de buses
 * - Implementación: DaoBus_Impl proporciona la lógica específica de BD
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package dao;

import model.Bus;
import java.util.List;

/**
 * DaoBus - Interfaz DAO para operaciones de buses
 * 
 * Define el contrato para todas las operaciones de acceso a datos relacionadas
 * con la gestión de buses del sistema de transporte público EnRoute AQP.
 * 
 * Implementa el patrón DAO para:
 * - Abstraer la lógica de acceso a datos de buses
 * - Permitir diferentes implementaciones de persistencia
 * - Facilitar testing mediante mocks
 * - Mantener separación entre lógica de negocio y acceso a datos
 * - Gestionar la flota de buses por rutas
 */
public interface DaoBus {
    
    // ========== OPERACIONES CRUD - PATRÓN DAO ==========
    
    /**
     * Registra un nuevo bus en el sistema
     * @param bus Objeto Bus con la información del vehículo a registrar
     * @throws Exception Si ocurre error en la operación de inserción
     */
    public void registrar(Bus bus) throws Exception;
    
    /**
     * Modifica la información de un bus existente
     * @param id Identificador único del bus a modificar
     * @param bus Objeto Bus con los nuevos datos
     * @throws Exception Si ocurre error en la operación de actualización
     */
    public void modificar(String id, Bus bus) throws Exception;
    
    /**
     * Elimina un bus del sistema
     * @param id Identificador único del bus a eliminar
     * @throws Exception Si ocurre error en la operación de eliminación
     */
    public void eliminar(String id) throws Exception;
    
    /**
     * Busca todos los buses asignados a una ruta específica
     * @param idRuta Identificador de la ruta para filtrar buses
     * @return List<Bus> con todos los buses de la ruta especificada
     * @throws Exception Si ocurre error en la consulta
     */
    public List<Bus> findByRuta(int idRuta) throws Exception;
}
