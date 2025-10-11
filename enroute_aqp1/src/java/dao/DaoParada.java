/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;


import model.Parada;
import java.util.List;

/**
 *
 * @author aapaz
 */
public interface DaoParada {
    public void registrar(Parada parada) throws Exception;
    public void modificar(int id, Parada parada) throws Exception;
    public void eliminar(int id) throws Exception;
    public List<Parada> listarPorRuta(int idRuta) throws Exception;
}
