/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utp;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Resource para la gestión del historial de viajes.
 * Endpoints expuestos en /api/historial-viajes
 * 
 * @author aapaz
 */
@Path("/api/historial-viajes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistorialViajesResource {
/*

    /**
     * GET /api/historial-viajes
     * Obtiene la lista de todos los viajes registrados (solo administradores).
     * 
     * @return Lista de viajes con status 200 OK
     */
    @GET
    public Response getAllViajes() {
        try {
            List<HistorialViajes> viajes = HistorialViajes.listAll();
            return Response.ok(viajes).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al listar viajes: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * GET /api/historial-viajes/{id}
     * Obtiene un viaje específico por su ID.
     * 
     * @param id ID del viaje
     * @return Viaje encontrado (200 OK) o 404 Not Found
     */
    @GET
    @Path("/{id}")
    public Response getViajeById(@PathParam("id") int id) {
        try {
            HistorialViajes viaje = HistorialViajes.findById(id);
            if (viaje == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Viaje no encontrado\"}")
                        .build();
            }
            return Response.ok(viaje).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al obtener viaje: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * GET /api/historial-viajes/usuario/{idUsuario}
     * Obtiene el historial de viajes de un usuario específico.
     * 
     * @param idUsuario ID del usuario
     * @return Lista de viajes del usuario (200 OK) o vacía si no hay viajes
     */
    @GET
    @Path("/usuario/{idUsuario}")
    public Response getViajesByUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            List<HistorialViajes> viajes = HistorialViajes.findByUsuario(idUsuario);
            return Response.ok(viajes).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al obtener historial del usuario: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * POST /api/historial-viajes
     * Registra un nuevo viaje en el historial.
     * 
     * @param nuevoViaje Datos del viaje a registrar
     * @return Viaje creado con status 201 CREATED o 400 Bad Request
     */
    @POST
    public Response registrarViaje(HistorialViajes nuevoViaje) {
        try {
            // Validar campos obligatorios
            if (nuevoViaje == null || nuevoViaje.getUsuario() == null || nuevoViaje.getUsuario().getId() == 0 || 
                nuevoViaje.getIdRuta() == 0 || nuevoViaje.getOrigen() == null || 
                nuevoViaje.getDestino() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Faltan datos requeridos: usuario (id), idRuta, origen, destino\"}")
                        .build();
            }

            // Establecer fecha de registro si no está configurada
            if (nuevoViaje.getFechaRegistro() == null) {
                nuevoViaje.setFechaRegistro(LocalDateTime.now());
            }

            // Establecer estado por defecto si no está configurado
            if (nuevoViaje.getEstado() == null) {
                nuevoViaje.setEstado("completado");
            }

            // Persistir el viaje
            nuevoViaje.persist();

            return Response.status(Response.Status.CREATED).entity(nuevoViaje).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al registrar viaje: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * PUT /api/historial-viajes/{id}
     * Actualiza los datos de un viaje registrado.
     * 
     * @param id ID del viaje
     * @param viajeActualizado Datos actualizados
     * @return Viaje actualizado (200 OK) o 404 Not Found
     */
    @PUT
    @Path("/{id}")
    public Response actualizarViaje(@PathParam("id") int id, HistorialViajes viajeActualizado) {
        try {
            HistorialViajes viaje = HistorialViajes.findById(id);
            if (viaje == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Viaje no encontrado\"}")
                        .build();
            }

            // Actualizar campos
            if (viajeActualizado.getOrigen() != null) viaje.setOrigen(viajeActualizado.getOrigen());
            if (viajeActualizado.getDestino() != null) viaje.setDestino(viajeActualizado.getDestino());
            if (viajeActualizado.getFechaViaje() != null) viaje.setFechaViaje(viajeActualizado.getFechaViaje());
            if (viajeActualizado.getDuracionMinutos() != null) viaje.setDuracionMinutos(viajeActualizado.getDuracionMinutos());
            if (viajeActualizado.getCosto() != null) viaje.setCosto(viajeActualizado.getCosto());
            if (viajeActualizado.getEstado() != null) viaje.setEstado(viajeActualizado.getEstado());

            viaje.persist();
            return Response.ok(viaje).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al actualizar viaje: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * DELETE /api/historial-viajes/{id}
     * Elimina un registro de viaje del historial.
     * 
     * @param id ID del viaje a eliminar
     * @return 204 No Content si se eliminó, 404 Not Found si no existe
     */
    @DELETE
    @Path("/{id}")
    public Response eliminarViaje(@PathParam("id") int id) {
        try {
            HistorialViajes viaje = HistorialViajes.findById(id);
            if (viaje == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Viaje no encontrado\"}")
                        .build();
            }

            viaje.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al eliminar viaje: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
