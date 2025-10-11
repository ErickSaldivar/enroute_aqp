/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import model.Bus;
import java.util.List;

/**
 *
 * @author aapaz
 */
public interface DaoBus {
    public void registrar(Bus bus) throws Exception;
    public void modificar(String id, Bus bus) throws Exception;
    public void eliminar(String id) throws Exception;
    public List<Bus> findByRuta(int idRuta) throws Exception;
}
