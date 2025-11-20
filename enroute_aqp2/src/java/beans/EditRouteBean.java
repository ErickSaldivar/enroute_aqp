package beans;

import dao.DaoRuta;
import dao.impl.DaoRuta_Impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Linea;
import models.Paradero;

@ManagedBean(name = "editRouteBean")
@ViewScoped
public class EditRouteBean implements Serializable {

    private int id; // route id
    private String nombre;
    private String descripcion;
    private List<Paradero> paraderos;

    // fields to add paradero
    private String newNombre;
    private String newDireccion;
    private Double newLat;
    private Double newLng;

    // selection
    private Paradero selectedParadero;

    public EditRouteBean() {
        paraderos = new ArrayList<>();
        // load id from request param
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            if (params.containsKey("id")) {
                id = Integer.parseInt(params.get("id"));
                load();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public void load() {
        DaoRuta dao = new DaoRuta_Impl();
        try {
            Linea l = dao.obtenerLineaPorId(id);
            if (l != null) {
                this.nombre = l.getNombre();
                this.descripcion = l.getDescripcion();
                this.paraderos = dao.listarParaderosPorLinea(id);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar la ruta"));
        }
    }

    public void saveLinea() {
        DaoRuta dao = new DaoRuta_Impl();
        try {
            if (id <= 0) {
                Linea lnew = new Linea(0, nombre, descripcion);
                int newId = dao.crearLinea(lnew);
                if (newId > 0) {
                    this.id = newId;
                    load();
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Creado", "Ruta creada correctamente"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear la ruta"));
                }
            } else {
                Linea l = new Linea(id, nombre, descripcion);
                dao.actualizarLinea(l);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardado", "Ruta actualizada"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar"));
        }
    }

    public void addParadero() {
        if (id <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Ruta no creada", "Primero guarda la ruta antes de agregar paraderos"));
            return;
        }
        if (newNombre == null || newNombre.trim().isEmpty() || newLat == null || newLng == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos incompletos", "Ingrese nombre y coordenadas"));
            return;
        }
        DaoRuta dao = new DaoRuta_Impl();
        try {
            Paradero p = new Paradero();
            p.setNombre(newNombre);
            p.setDireccion(newDireccion);
            p.setLatitud(newLat);
            p.setLongitud(newLng);
            Paradero inserted = dao.agregarParadero(id, p);
            this.paraderos.add(inserted);
            // reset fields
            newNombre = null; newDireccion = null; newLat = null; newLng = null;
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Paradero agregado", "Se agregó el paradero"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo agregar el paradero"));
        }
    }

    public void rcUpdateCoords() {
        Map<String, String> m = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            int pid = Integer.parseInt(m.get("paraderoId"));
            double lat = Double.parseDouble(m.get("lat"));
            double lng = Double.parseDouble(m.get("lng"));
            Paradero target = null;
            for (Paradero p : paraderos) { if (p.getId() == pid) { target = p; break; } }
            if (target != null) {
                target.setLatitud(lat);
                target.setLongitud(lng);
                new DaoRuta_Impl().actualizarParadero(target);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public void updateParadero() {
        if (this.selectedParadero == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccionar", "Seleccione un paradero de la tabla para actualizar"));
            return;
        }
        try {
            // apply changes from the input fields to the selectedParadero
            if (this.newNombre != null) this.selectedParadero.setNombre(this.newNombre);
            this.selectedParadero.setDireccion(this.newDireccion);
            if (this.newLat != null) this.selectedParadero.setLatitud(this.newLat);
            if (this.newLng != null) this.selectedParadero.setLongitud(this.newLng);
            new DaoRuta_Impl().actualizarParadero(this.selectedParadero);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Paradero actualizado correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el paradero"));
        }
    }

    public void deleteParadero() {
        if (this.selectedParadero == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccionar", "Seleccione un paradero de la tabla para eliminar"));
            return;
        }
        try {
            int pid = this.selectedParadero.getId();
            new DaoRuta_Impl().eliminarParadero(this.id, pid);
            // remove from local list
            Paradero toRemove = null;
            for (Paradero p : paraderos) { if (p.getId() == pid) { toRemove = p; break; } }
            if (toRemove != null) paraderos.remove(toRemove);
            // clear selection and input fields
            this.selectedParadero = null;
            this.newNombre = null; this.newDireccion = null; this.newLat = null; this.newLng = null;
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "Paradero eliminado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el paradero"));
        }
    }

    public void onRowSelect() {
        try {
            Paradero p = this.selectedParadero;
            if (p != null) {
                this.newNombre = p.getNombre();
                this.newDireccion = p.getDireccion();
                this.newLat = p.getLatitud();
                this.newLng = p.getLongitud();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public String getRouteJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":").append(id).append(",\"paraderos\":[");
        for (int i = 0; i < paraderos.size(); i++) {
            Paradero p = paraderos.get(i);
            sb.append("{\"id\":").append(p.getId())
              .append(",\"nombre\":\"").append(escape(p.getNombre())).append("\"")
              .append(",\"lat\":").append(p.getLatitud())
              .append(",\"lng\":").append(p.getLongitud())
              .append(",\"orden\":").append(p.getOrden())
              .append("}");
            if (i < paraderos.size() - 1) sb.append(',');
        }
        sb.append("]}");
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Paradero> getParaderos() { return paraderos; }
    public void setParaderos(List<Paradero> paraderos) { this.paraderos = paraderos; }
    public String getNewNombre() { return newNombre; }
    public void setNewNombre(String newNombre) { this.newNombre = newNombre; }
    public String getNewDireccion() { return newDireccion; }
    public void setNewDireccion(String newDireccion) { this.newDireccion = newDireccion; }
    public Double getNewLat() { return newLat; }
    public void setNewLat(Double newLat) { this.newLat = newLat; }
    public Double getNewLng() { return newLng; }
    public void setNewLng(Double newLng) { this.newLng = newLng; }
    public Paradero getSelectedParadero() { return selectedParadero; }
    public void setSelectedParadero(Paradero selectedParadero) { this.selectedParadero = selectedParadero; }
}
