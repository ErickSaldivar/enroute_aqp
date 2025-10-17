/*
 * ========== INTERFACE DAOPARADA - PATRÓN DAO PARA PARADAS ==========
 * 
 * Esta interfaz implementa el patrón DAO (Data Access Object) para la gestión de paradas:
 * 
 * PATRONES DE DISEÑO APLICADOS:
 * 1. DAO Pattern: Abstrae el acceso a datos de paradas del sistema de transporte
 * 2. Interface Segregation: Define contrato específico para operaciones de paradas
 * 3. Dependency Inversion: Permite diferentes implementaciones de persistencia
 * 4. Repository Pattern: Encapsula lógica de acceso a repositorio de paradas
 * 
 * CONCEPTOS IMPLEMENTADOS:
 * - Abstracción: Define operaciones sin implementación específica
 * - Polimorfismo: Permite múltiples implementaciones de base de datos
 * - Separación de responsabilidades: Solo operaciones de acceso a datos de paradas
 * - Inversión de dependencias: Código depende de abstracción, no implementación
 * 
 * OPERACIONES CRUD IMPLEMENTADAS:
 * - Create: registrar() - Registra nuevas paradas en el sistema
 * - Read: listarPorRuta() - Lista paradas filtradas por ruta específica
 * - Update: modificar() - Actualiza información de paradas existentes
 * - Delete: eliminar() - Elimina paradas del sistema
 * 
 * FUNCIONALIDADES DE NEGOCIO:
 * - Gestión de paradas de transporte público
 * - Geolocalización de puntos de parada
 * - Asignación de paradas a rutas específicas
 * - Control de ubicaciones y direcciones de paradas
 * 
 * INTEGRACIÓN CON MVC:
 * - Modelo: Define la capa de acceso a datos de paradas
 * - Controlador: Usado por servlets de administración de paradas
 * - Implementación: DaoParada_Impl proporciona la lógica específica de BD
 * 
 * @author Equipo EnRoute AQP
 * @version 2.0 - Segundo Avance
 */
package dao;


import model.Parada;
import java.util.List;

/**
 * DaoParada - Interfaz DAO para operaciones de paradas
 * 
 * Define el contrato para todas las operaciones de acceso a datos relacionadas
 * con la gestión de paradas del sistema de transporte público EnRoute AQP.
 * 
 * Implementa el patrón DAO para:
 * - Abstraer la lógica de acceso a datos de paradas
 * - Permitir diferentes implementaciones de persistencia
 * - Facilitar testing mediante mocks
 * - Mantener separación entre lógica de negocio y acceso a datos
 * - Gestionar el sistema de paradas y ubicaciones
 */
public interface DaoParada {
    
    // ========== OPERACIONES CRUD - PATRÓN DAO ==========
    
    /**
     * Registra una nueva parada en el sistema
     * @param parada Objeto Parada con la información de ubicación a registrar
     * @throws Exception Si ocurre error en la operación de inserción
     */
    public void registrar(Parada parada) throws Exception;
    
    /**
     * Modifica la información de una parada existente
     * @param id Identificador único de la parada a modificar
     * @param parada Objeto Parada con los nuevos datos
     * @throws Exception Si ocurre error en la operación de actualización
     */
    public void modificar(int id, Parada parada) throws Exception;
    
    /**
     * Elimina una parada del sistema
     * @param id Identificador único de la parada a eliminar
     * @throws Exception Si ocurre error en la operación de eliminación
     */
    public void eliminar(int id) throws Exception;
    
    /**
     * Lista todas las paradas asociadas a una ruta específica
     * @param idRuta Identificador de la ruta para filtrar paradas
     * @return List<Parada> con todas las paradas de la ruta especificada
     * @throws Exception Si ocurre error en la consulta
     */
    public List<Parada> listarPorRuta(int idRuta) throws Exception;
}
