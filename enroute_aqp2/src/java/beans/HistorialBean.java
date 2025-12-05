package beans;

import dao.DaoHistorial;
import dao.impl.DaoHistorial_Impl;
import models.Historial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * Bean para gestionar el historial de viajes del usuario
 */
@ManagedBean(name = "historialBean")
@ViewScoped
public class HistorialBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<Historial> historialCompleto;
    private Map<String, List<Historial>> historialAgrupado;
    private int totalViajes;
    private int totalHoras;
    private double gastoTotal;
    private int destinosUnicos;
    
    // Campos temporales para guardar viaje desde el formulario
    private int tempIdLinea;
    private String tempParaderoSubida;
    private String tempParaderoBajada;
    private String tempNombreLinea;
    private int tempTiempoEstimado;
    
    public HistorialBean() {
        cargarHistorial();
    }
    
    private void cargarHistorial() {
        try {
            Object userIdObj = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("userId");
            
            if (userIdObj != null) {
                int userId = (Integer) userIdObj;
                DaoHistorial dao = new DaoHistorial_Impl();
                historialCompleto = dao.obtenerHistorialPorUsuario(userId);
                agruparHistorial();
                calcularEstadisticas();
            } else {
                historialCompleto = new ArrayList<>();
                historialAgrupado = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            historialCompleto = new ArrayList<>();
            historialAgrupado = new HashMap<>();
        }
    }
    
    private void agruparHistorial() {
        historialAgrupado = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String hoy = sdf.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String ayer = sdf.format(cal.getTime());
        
        for (Historial h : historialCompleto) {
            String fechaViaje = sdf.format(h.getFechaViaje());
            String grupo;
            
            if (fechaViaje.equals(hoy)) {
                grupo = "Hoy";
            } else if (fechaViaje.equals(ayer)) {
                grupo = "Ayer";
            } else {
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
                grupo = displayFormat.format(h.getFechaViaje());
            }
            
            historialAgrupado.computeIfAbsent(grupo, k -> new ArrayList<>()).add(h);
        }
    }
    
    private void calcularEstadisticas() {
        totalViajes = historialCompleto.size();
        totalHoras = 0;
        gastoTotal = 0.0;
        Set<String> destinos = new HashSet<>();
        
        for (Historial h : historialCompleto) {
            totalHoras += h.getTiempoEstimado();
            gastoTotal += h.getPrecio();
            destinos.add(h.getParaderoBajada());
        }
        
        destinosUnicos = destinos.size();
    }
    
    public String guardarViaje(int idLinea, String paraderoSubida, String paraderoBajada, 
                               String nombreLinea, int tiempoEstimado) {
        try {
            Object userIdObj = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("userId");
            
            if (userIdObj == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "Debe iniciar sesión para guardar el viaje"));
                return null;
            }
            
            int userId = (Integer) userIdObj;
            
            Historial historial = new Historial();
            historial.setIdUsuario(userId);
            historial.setIdLinea(idLinea);
            historial.setParaderoSubida(paraderoSubida);
            historial.setParaderoBajada(paraderoBajada);
            historial.setNombreLinea(nombreLinea);
            historial.setTiempoEstimado(tiempoEstimado);
            historial.setPrecio(1.00); // Precio por defecto
            
            DaoHistorial dao = new DaoHistorial_Impl();
            boolean guardado = dao.guardarViaje(historial);
            
            if (guardado) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Éxito", "Viaje guardado en el historial"));
                cargarHistorial(); // Recargar historial
                return "success";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error", "No se pudo guardar el viaje"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "Ocurrió un error al guardar el viaje"));
            return null;
        }
    }
    
    public String limpiarHistorial() {
        try {
            Object userIdObj = FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("userId");
            
            if (userIdObj != null) {
                int userId = (Integer) userIdObj;
                DaoHistorial dao = new DaoHistorial_Impl();
                boolean eliminado = dao.eliminarHistorialUsuario(userId);
                
                if (eliminado) {
                    cargarHistorial();
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Éxito", "Historial limpiado correctamente"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Error", "No se pudo limpiar el historial"));
        }
        return null;
    }
    
    public String guardarViajeDesdeForm() {
        return guardarViaje(tempIdLinea, tempParaderoSubida, tempParaderoBajada, 
                           tempNombreLinea, tempTiempoEstimado);
    }
    
    public String formatearHora(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(fecha);
    }
    
    // Getters
    public List<Historial> getHistorialCompleto() {
        return historialCompleto;
    }
    
    public Map<String, List<Historial>> getHistorialAgrupado() {
        return historialAgrupado;
    }
    
    public int getTotalViajes() {
        return totalViajes;
    }
    
    public int getTotalHoras() {
        return totalHoras;
    }
    
    public double getGastoTotal() {
        return gastoTotal;
    }
    
    public int getDestinosUnicos() {
        return destinosUnicos;
    }
    
    // Getters y Setters para campos temporales
    public int getTempIdLinea() {
        return tempIdLinea;
    }
    
    public void setTempIdLinea(int tempIdLinea) {
        this.tempIdLinea = tempIdLinea;
    }
    
    public String getTempParaderoSubida() {
        return tempParaderoSubida;
    }
    
    public void setTempParaderoSubida(String tempParaderoSubida) {
        this.tempParaderoSubida = tempParaderoSubida;
    }
    
    public String getTempParaderoBajada() {
        return tempParaderoBajada;
    }
    
    public void setTempParaderoBajada(String tempParaderoBajada) {
        this.tempParaderoBajada = tempParaderoBajada;
    }
    
    public String getTempNombreLinea() {
        return tempNombreLinea;
    }
    
    public void setTempNombreLinea(String tempNombreLinea) {
        this.tempNombreLinea = tempNombreLinea;
    }
    
    public int getTempTiempoEstimado() {
        return tempTiempoEstimado;
    }
    
    public void setTempTiempoEstimado(int tempTiempoEstimado) {
        this.tempTiempoEstimado = tempTiempoEstimado;
    }
}
