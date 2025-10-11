
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;
import dao.DaoRuta;
import model.Ruta;
import dao.DBConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author aapaz
 */
public class DaoRuta_Impl implements DaoRuta{

       @Override
    public void registrar(Ruta ruta) throws SQLException {
        String sql = "INSERT INTO rutas(nombre_ruta, descripcion, punto_inicio, punto_fin, tiempo_estimado) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ruta.getNombre());
           
            ps.setInt(2, ruta.getTiempoEstimado());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar la ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificar(int id, Ruta ruta) throws SQLException {
        String sql = "UPDATE rutas SET nombre_ruta = ?, tiempo_estimado = ? WHERE id_ruta = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ruta.getNombre());
            ps.setInt(2, ruta.getTiempoEstimado());
            ps.setInt(3, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar la ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM rutas WHERE id_ruta = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la ruta: " + e.getMessage(), e);
        }
    }

   
    @Override
    public List<Ruta> listarParadas() throws SQLException {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT id_ruta, nombre_ruta, tiempo_estimado FROM rutas";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_ruta");
                String nombre = rs.getString("nombre_ruta");
                int tiempoEstimado = rs.getInt("tiempo_estimado");
                
                Ruta ruta = new Ruta(id, nombre, tiempoEstimado);
                rutas.add(ruta);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las rutas: " + e.getMessage(), e);
        }
        return rutas;
    }

}
