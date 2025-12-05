package dao;

import models.Historial;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para el acceso a datos del historial de viajes
 */
public interface DaoHistorial {
    
    /**
     * Guarda un nuevo viaje en el historial
     * @param historial el objeto Historial a guardar
     * @return true si se guardó correctamente, false en caso contrario
     * @throws SQLException si hay un error en la base de datos
     */
    boolean guardarViaje(Historial historial) throws SQLException;
    
    /**
     * Obtiene el historial de viajes de un usuario
     * @param idUsuario el ID del usuario
     * @return lista de viajes del usuario ordenados por fecha descendente
     * @throws SQLException si hay un error en la base de datos
     */
    List<Historial> obtenerHistorialPorUsuario(int idUsuario) throws SQLException;
    
    /**
     * Obtiene el historial de viajes de un usuario limitado a los N más recientes
     * @param idUsuario el ID del usuario
     * @param limite número máximo de registros a retornar
     * @return lista de viajes del usuario ordenados por fecha descendente
     * @throws SQLException si hay un error en la base de datos
     */
    List<Historial> obtenerHistorialReciente(int idUsuario, int limite) throws SQLException;
    
    /**
     * Elimina todo el historial de un usuario
     * @param idUsuario el ID del usuario
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws SQLException si hay un error en la base de datos
     */
    boolean eliminarHistorialUsuario(int idUsuario) throws SQLException;
}
