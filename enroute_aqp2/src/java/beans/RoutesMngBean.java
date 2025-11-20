package beans;

import dao.DaoRuta;
import dao.impl.DaoRuta_Impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Linea;
import models.Paradero;

@ManagedBean(name = "routesMngBean")
@ViewScoped
public class RoutesMngBean implements Serializable {

    private List<Linea> lineas;
    private Map<Integer, List<Paradero>> cacheParaderos;

    public RoutesMngBean() {
        cacheParaderos = new HashMap<>();
        cargarLineas();
    }

    public void cargarLineas() {
        DaoRuta dao = new DaoRuta_Impl();
        try {
            lineas = dao.listarLineas();
        } catch (Exception e) {
            lineas = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las rutas"));
        }
    }

    public List<Linea> getLineas() { return lineas; }
    public void setLineas(List<Linea> lineas) { this.lineas = lineas; }

    public List<Paradero> getParaderos(int idLinea) {
        if (cacheParaderos.containsKey(idLinea)) return cacheParaderos.get(idLinea);
        DaoRuta dao = new DaoRuta_Impl();
        try {
            List<Paradero> ps = dao.listarParaderosPorLinea(idLinea);
            cacheParaderos.put(idLinea, ps);
            return ps;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // EL-friendly method name for use like routesMngBean.paraderos(ln.id)
    public List<Paradero> paraderos(int idLinea) {
        return getParaderos(idLinea);
    }

    public void deleteLinea(int idLinea) {
        DaoRuta dao = new DaoRuta_Impl();
        try {
            dao.eliminarLinea(idLinea);
            cacheParaderos.remove(idLinea);
            cargarLineas();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "La ruta fue eliminada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la ruta"));
        }
    }

    public String getRoutesJson() {
        // Construye JSON con lineas y sus paraderos
        StringBuilder sb = new StringBuilder();
        sb.append("{\"lineas\":[");
        for (int i = 0; i < (lineas == null ? 0 : lineas.size()); i++) {
            Linea l = lineas.get(i);
            sb.append("{\"id\":").append(l.getId())
              .append(",\"nombre\":\"").append(escape(l.getNombre())).append("\"")
              .append(",\"descripcion\":\"").append(escape(l.getDescripcion())).append("\"")
              .append(",\"paraderos\":[");
            List<Paradero> ps = getParaderos(l.getId());
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
            if (i < lineas.size() - 1) sb.append(',');
        }
        sb.append("]}");
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
