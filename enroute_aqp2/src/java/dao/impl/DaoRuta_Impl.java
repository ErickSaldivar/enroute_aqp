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

    @Override
    public void eliminarLinea(int idLinea) throws Exception {
        String sqlLP = "DELETE FROM linea_paraderos WHERE id_linea = ?";
        String sqlL = "DELETE FROM lineas WHERE id_linea = ?";
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement ps1 = c.prepareStatement(sqlLP)) {
                ps1.setInt(1, idLinea);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = c.prepareStatement(sqlL)) {
                ps2.setInt(1, idLinea);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error eliminando linea " + idLinea + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Linea obtenerLineaPorId(int idLinea) throws Exception {
        String sql = "SELECT id_linea, nombre, descripcion FROM lineas WHERE id_linea = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLinea);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Linea(rs.getInt("id_linea"), rs.getString("nombre"), rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error obteniendo linea: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int crearLinea(Linea linea) throws Exception {
        String sql = "INSERT INTO lineas (nombre, descripcion) VALUES (?, ?)";
        String getId = "SELECT LAST_INSERT_ID()";
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, linea.getNombre());
                ps.setString(2, linea.getDescripcion());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(getId); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Error creando linea: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public void actualizarLinea(Linea linea) throws Exception {
        String sql = "UPDATE lineas SET nombre = ?, descripcion = ? WHERE id_linea = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, linea.getNombre());
            ps.setString(2, linea.getDescripcion());
            ps.setInt(3, linea.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error actualizando linea: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarParadero(Paradero p) throws Exception {
        String sql = "UPDATE paraderos SET nombre = ?, direccion = ?, latitud = ?, longitud = ? WHERE id_paradero = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.setDouble(3, p.getLatitud());
            ps.setDouble(4, p.getLongitud());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error actualizando paradero: " + e.getMessage(), e);
        }
    }

    @Override
    public Paradero agregarParadero(int idLinea, Paradero p) throws Exception {
        String insertParadero = "INSERT INTO paraderos (nombre, direccion, latitud, longitud) VALUES (?, ?, ?, ?)";
        String getId = "SELECT LAST_INSERT_ID()";
        String nextOrdenSql = "SELECT COALESCE(MAX(orden),0)+1 AS nextOrden FROM linea_paraderos WHERE id_linea = ?";
        String insertLP = "INSERT INTO linea_paraderos (id_linea, id_paradero, orden) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            int newParaderoId = 0;
            try (PreparedStatement ps = c.prepareStatement(insertParadero)) {
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDireccion());
                ps.setDouble(3, p.getLatitud());
                ps.setDouble(4, p.getLongitud());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement(getId);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) newParaderoId = rs.getInt(1);
            }
            int nextOrden = 1;
            try (PreparedStatement ps = c.prepareStatement(nextOrdenSql)) {
                ps.setInt(1, idLinea);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) nextOrden = rs.getInt("nextOrden");
                }
            }
            try (PreparedStatement ps = c.prepareStatement(insertLP)) {
                ps.setInt(1, idLinea);
                ps.setInt(2, newParaderoId);
                ps.setInt(3, nextOrden);
                ps.executeUpdate();
            }
            c.commit();
            Paradero nuevo = new Paradero();
            nuevo.setId(newParaderoId);
            nuevo.setNombre(p.getNombre());
            nuevo.setDireccion(p.getDireccion());
            nuevo.setLatitud(p.getLatitud());
            nuevo.setLongitud(p.getLongitud());
            nuevo.setOrden(nextOrden);
            return nuevo;
        } catch (SQLException e) {
            throw new SQLException("Error agregando paradero: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarParadero(int idLinea, int idParadero) throws Exception {
        String delLP = "DELETE FROM linea_paraderos WHERE id_linea = ? AND id_paradero = ?";
        String countSql = "SELECT COUNT(*) AS cnt FROM linea_paraderos WHERE id_paradero = ?";
        String delP = "DELETE FROM paraderos WHERE id_paradero = ?";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(delLP)) {
                ps.setInt(1, idLinea);
                ps.setInt(2, idParadero);
                ps.executeUpdate();
            }
            int count = 0;
            try (PreparedStatement ps = c.prepareStatement(countSql)) {
                ps.setInt(1, idParadero);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) count = rs.getInt("cnt");
                }
            }
            if (count == 0) {
                try (PreparedStatement ps = c.prepareStatement(delP)) {
                    ps.setInt(1, idParadero);
                    ps.executeUpdate();
                }
            }
            c.commit();
        } catch (SQLException e) {
            throw new SQLException("Error eliminando paradero: " + e.getMessage(), e);
        }
    }
}
