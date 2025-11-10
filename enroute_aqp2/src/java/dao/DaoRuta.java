package dao;

import java.util.List;
import models.Linea;
import models.Paradero;

public interface DaoRuta {
    List<Linea> listarLineas() throws Exception;
    List<Paradero> listarParaderosPorLinea(int idLinea) throws Exception;
}
