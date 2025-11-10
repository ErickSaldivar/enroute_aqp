package beans;

import dao.DaoRuta;
import dao.impl.DaoRuta_Impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import models.Linea;
import models.Paradero;

@ManagedBean(name = "rutasBean")
@RequestScoped
public class RutasBean implements Serializable {

    private List<Linea> lineas;
    // Cache de paraderos por línea (opcional)
    private List<RutaCompleta> rutas;

    public RutasBean() {
        DaoRuta dao = new DaoRuta_Impl();
        try {
            this.lineas = dao.listarLineas();
            this.rutas = new ArrayList<>();
            for (Linea l : lineas) {
                List<Paradero> paraderos = dao.listarParaderosPorLinea(l.getId());
                rutas.add(new RutaCompleta(l, paraderos));
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.lineas = new ArrayList<>();
            this.rutas = new ArrayList<>();
        }
    }

    public List<Linea> getLineas() { return lineas; }

    public String getRoutesJson() {
        // Construir JSON simple: {"lineas":[{"id":..,"nombre":"..","descripcion":"..","paraderos":[{"id":..,"nombre":"..","lat":..,"lng":..,"orden":..},..]},..]}
        StringBuilder sb = new StringBuilder();
        sb.append("{\"lineas\":[");
        for (int i = 0; i < rutas.size(); i++) {
            RutaCompleta r = rutas.get(i);
            Linea l = r.linea;
            sb.append("{\"id\":").append(l.getId())
              .append(",\"nombre\":\"").append(escape(l.getNombre())).append("\"")
              .append(",\"descripcion\":\"").append(escape(l.getDescripcion())).append("\"")
              .append(",\"paraderos\":[");
            List<Paradero> ps = r.paraderos;
            for (int j = 0; j < ps.size(); j++) {
                Paradero p = ps.get(j);
                sb.append("{\"id\":").append(p.getId())
                  .append(",\"nombre\":\"").append(escape(p.getNombre())).append("\"")
                  .append(",\"lat\":").append(p.getLatitud())
                  .append(",\"lng\":").append(p.getLongitud())
                  .append(",\"orden\":").append(p.getOrden())
                  .append("}");
                if (j < ps.size() - 1) sb.append(',');
            }
            sb.append("]}");
            if (i < rutas.size() - 1) sb.append(',');
        }
        sb.append("]}");
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Helper DTO to hold full route
    private static class RutaCompleta {
        Linea linea;
        List<Paradero> paraderos;
        RutaCompleta(Linea l, List<Paradero> p) { this.linea = l; this.paraderos = p; }
    }
}
