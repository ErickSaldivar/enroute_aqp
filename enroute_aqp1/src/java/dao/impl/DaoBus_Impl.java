/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;
import java.sql.*;
import dao.DBConnection;
import model.Bus;
import dao.DaoBus;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author aapaz
 */
public class DaoBus_Impl implements DaoBus{
    @Override
    public void registrar(Bus bus) throws Exception {
       String sql = "INSERT INTO buses(id, id_ruta, capacidad) VALUES(?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, bus.getId());      
        ps.setString(2, bus.getLinea());   
        ps.setInt(3, bus.getCapacidad());

        ps.executeUpdate();

    } catch (SQLException e) {
        throw new SQLException("Error al registrar el bus: " + e.getMessage(), e);
    }
    }

    @Override
    public void modificar(String id, Bus bus) throws Exception {
        String sql = "UPDATE buses SET placa=?, id_ruta=?, capacidad=? WHERE placa=?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, bus.getId());
            ps.setString(2, bus.getLinea());
            ps.setInt(3, bus.getCapacidad());
            ps.setString(4, id);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al modificar el bus: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String id) throws Exception {
         String sql = "DELETE FROM buses WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el bus: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Bus> findByRuta(int idRuta) throws Exception {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE id_ruta=?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRuta);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bus bus = new Bus();
                    bus.setId(rs.getString("id"));
                    bus.setLinea(rs.getString("id_ruta"));
                    bus.setCapacidad(rs.getInt("capacidad"));
                    buses.add(bus);
                }
            }
            
        } catch (SQLException e) {
            throw new SQLException("Error al buscar buses por ruta: " + e.getMessage(), e);
        }
        
        return buses;
    }
}
