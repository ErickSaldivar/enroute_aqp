package dao.impl;

import dao.DaoHistorial;
import dao.DBConnection;
import models.Historial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de DaoHistorial para gestionar el historial de viajes
 */
public class DaoHistorial_Impl implements DaoHistorial {
    
    @Override
    public boolean guardarViaje(Historial historial) throws SQLException {
        String sql = "INSERT INTO historial_viajes (id_usuario, id_linea, paradero_subida, " +
                     "paradero_bajada, nombre_linea, tiempo_estimado, precio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, historial.getIdUsuario());
            ps.setInt(2, historial.getIdLinea());
            ps.setString(3, historial.getParaderoSubida());
            ps.setString(4, historial.getParaderoBajada());
            ps.setString(5, historial.getNombreLinea());
            ps.setInt(6, historial.getTiempoEstimado());
            ps.setDouble(7, historial.getPrecio());
            
            int result = ps.executeUpdate();
            return result > 0;
        } finally {
            if (ps != null) ps.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Historial> obtenerHistorialPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM historial_viajes WHERE id_usuario = ? " +
                     "ORDER BY fecha_viaje DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Historial> lista = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Historial h = new Historial();
                h.setId(rs.getInt("id"));
                h.setIdUsuario(rs.getInt("id_usuario"));
                h.setIdLinea(rs.getInt("id_linea"));
                h.setParaderoSubida(rs.getString("paradero_subida"));
                h.setParaderoBajada(rs.getString("paradero_bajada"));
                h.setNombreLinea(rs.getString("nombre_linea"));
                h.setTiempoEstimado(rs.getInt("tiempo_estimado"));
                h.setPrecio(rs.getDouble("precio"));
                h.setFechaViaje(rs.getTimestamp("fecha_viaje"));
                lista.add(h);
            }
            return lista;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Historial> obtenerHistorialReciente(int idUsuario, int limite) throws SQLException {
        String sql = "SELECT * FROM historial_viajes WHERE id_usuario = ? " +
                     "ORDER BY fecha_viaje DESC LIMIT ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Historial> lista = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, limite);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Historial h = new Historial();
                h.setId(rs.getInt("id"));
                h.setIdUsuario(rs.getInt("id_usuario"));
                h.setIdLinea(rs.getInt("id_linea"));
                h.setParaderoSubida(rs.getString("paradero_subida"));
                h.setParaderoBajada(rs.getString("paradero_bajada"));
                h.setNombreLinea(rs.getString("nombre_linea"));
                h.setTiempoEstimado(rs.getInt("tiempo_estimado"));
                h.setPrecio(rs.getDouble("precio"));
                h.setFechaViaje(rs.getTimestamp("fecha_viaje"));
                lista.add(h);
            }
            return lista;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminarHistorialUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM historial_viajes WHERE id_usuario = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            
            int result = ps.executeUpdate();
            return result > 0;
        } finally {
            if (ps != null) ps.close();
            DBConnection.closeConnection(conn);
        }
    }
}
