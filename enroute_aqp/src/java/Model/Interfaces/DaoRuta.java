/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.Interfaces;

import Model.Ruta;
import java.util.List;

/**
 *
 * @author aapaz
 */
public interface DaoRuta {
    public void registrar(Ruta ruta) throws Exception;
    public void modificar(int id, Ruta ruta) throws Exception;
    public void eliminar(int id) throws Exception;
    public List<Ruta> listarParadas() throws Exception;
}
