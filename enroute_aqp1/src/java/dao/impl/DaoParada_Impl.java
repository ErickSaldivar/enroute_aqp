/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;
import model.*;
import dao.DaoParada;
import dao.DBConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author aapaz
 */
public class DaoParada_Impl implements DaoParada{

    @Override
    public void registrar(Parada parada) throws Exception {
        
        String sql = "INSERT INTO paradas(nombre_parada, latitud, longitud, referencia) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parada.getNombre());
            ps.setDouble(2, parada.getLatitud());
            ps.setDouble(3, parada.getLongitud());
            ps.setString(4, parada.getDireccion());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar la parada: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificar(int id, Parada parada) throws Exception {
        String sql = "UPDATE paradas SET nombre_parada = ?, latitud = ?, longitud = ?, referencia = ? WHERE id_parada = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parada.getNombre());
            ps.setDouble(2, parada.getLatitud());
            ps.setDouble(3, parada.getLongitud());
            ps.setString(4, parada.getDireccion());
            ps.setInt(5, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar la parada: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM paradas WHERE id_parada = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la parada: " + e.getMessage(), e);
        }
    }

   
    @Override
    public List<Parada> listarPorRuta(int idRuta) throws Exception {
        List<Parada> paradas = new ArrayList<>();
        // Se unen las tablas 'paradas' (p) y 'rutas_paradas' (rp)
        String sql = "SELECT p.id_parada, p.nombre_parada, p.latitud, p.longitud, p.referencia " +
                     "FROM paradas p " +
                     "JOIN rutas_paradas rp ON p.id_parada = rp.id_parada " +
                     "WHERE rp.id_ruta = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idRuta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_parada");
                    String nombre = rs.getString("nombre_parada");
                    double latitud = rs.getDouble("latitud");
                    double longitud = rs.getDouble("longitud");
                    String direccion = rs.getString("referencia");

                    Parada parada = new Parada(id, nombre, direccion, latitud, longitud);
                    paradas.add(parada);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las paradas por ruta: " + e.getMessage(), e);
        }
        return paradas;
    }
    
}
