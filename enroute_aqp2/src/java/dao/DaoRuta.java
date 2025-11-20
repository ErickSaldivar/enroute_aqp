package dao;

import java.util.List;
import models.Linea;
import models.Paradero;

public interface DaoRuta {
    List<Linea> listarLineas() throws Exception;
    List<Paradero> listarParaderosPorLinea(int idLinea) throws Exception;
    void eliminarLinea(int idLinea) throws Exception;
    Linea obtenerLineaPorId(int idLinea) throws Exception;
    int crearLinea(Linea linea) throws Exception;
    void eliminarParadero(int idLinea, int idParadero) throws Exception;
    void actualizarLinea(Linea linea) throws Exception;
    void actualizarParadero(Paradero p) throws Exception;
    Paradero agregarParadero(int idLinea, Paradero p) throws Exception;
}
