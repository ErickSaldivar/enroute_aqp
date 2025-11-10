package dao.impl;

import dao.DBConnection;
import dao.DaoRuta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Linea;
import models.Paradero;

public class DaoRuta_Impl implements DaoRuta {

    @Override
    public List<Linea> listarLineas() throws Exception {
        String sql = "SELECT id_linea, nombre, descripcion FROM lineas ORDER BY id_linea";
        List<Linea> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Linea l = new Linea(rs.getInt("id_linea"), rs.getString("nombre"), rs.getString("descripcion"));
                list.add(l);
            }
        } catch (SQLException e) {
            throw new SQLException("Error listando lineas: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Paradero> listarParaderosPorLinea(int idLinea) throws Exception {
        String sql = "SELECT p.id_paradero, p.nombre, p.direccion, p.latitud, p.longitud, lp.orden " +
                     "FROM linea_paraderos lp " +
                     "INNER JOIN paraderos p ON lp.id_paradero = p.id_paradero " +
                     "WHERE lp.id_linea = ? ORDER BY lp.orden";
        List<Paradero> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLinea);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paradero p = new Paradero();
                    p.setId(rs.getInt("id_paradero"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDireccion(rs.getString("direccion"));
                    p.setLatitud(rs.getDouble("latitud"));
                    p.setLongitud(rs.getDouble("longitud"));
                    p.setOrden(rs.getInt("orden"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error listando paraderos de linea " + idLinea + ": " + e.getMessage(), e);
        }
        return list;
    }
}
