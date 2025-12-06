/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utp;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Resource para la gestión de usuarios.
 * Endpoints expuestos en /api/users
 * 
 * @author aapaz
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    /**
     * GET /api/users
     * Obtiene la lista de todos los usuarios registrados.
     * 
     * @return Lista de usuarios con status 200 OK
     */
    @GET
    @Transactional
    public Response getAllUsers() {
        try {
            List<User> users = User.listAll();
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al listar usuarios: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * GET /api/users/{id}
     * Obtiene un usuario específico por su ID.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado (200 OK) o 404 Not Found
     */
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al obtener usuario: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * POST /api/users/login
     * Autentica un usuario con email y contraseña hasheada.
     * 
     * @param loginUser Objeto con email y passwordHash
     * @return Usuario autenticado (200 OK) o 401 Unauthorized
     */
    @POST
    @Path("/login")
    public Response login(User loginUser) {
        try {
            if (loginUser == null || loginUser.getEmail() == null || loginUser.getPasswordHash() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Email y contraseña son requeridos\"}")
                        .build();
            }

            User user = User.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPasswordHash());
            if (user != null) {
                return Response.ok(user).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Credenciales inválidas\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error en login: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * POST /api/users
     * Registra un nuevo usuario.
     * 
     * @param newUser Datos del nuevo usuario
     * @return Usuario creado (201 CREATED) o 400/409 en caso de error
     */
    @POST
    @Transactional
    public Response createUser(User newUser) {
        try {
            // Validar campos obligatorios
            if (newUser == null || newUser.getNombre() == null || newUser.getApellido() == null ||
                newUser.getEmail() == null || newUser.getPasswordHash() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Campos requeridos: nombre, apellido, email, passwordHash\"}")
                        .build();
            }

            // Verificar si el email ya existe
            User existingUser = User.find("email = ?1", newUser.getEmail()).firstResult();
            if (existingUser != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"El email ya está registrado\"}")
                        .build();
            }

            // Establecer fecha de registro y rol por defecto
            newUser.setFechaRegistro(LocalDateTime.now());
            newUser.setEsAdmin(false); // Por defecto, no admin

            newUser.persist();
            return Response.status(Response.Status.CREATED).entity(newUser).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al crear usuario: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * PUT /api/users/{id}
     * Actualiza completamente los datos de un usuario.
     * 
     * @param id ID del usuario
     * @param updatedUser Datos actualizados
     * @return Usuario actualizado (200 OK) o 404 Not Found
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") int id, User updatedUser) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }

            // Actualizar campos (excepto id y fechaRegistro)
            if (updatedUser.getNombre() != null) user.setNombre(updatedUser.getNombre());
            if (updatedUser.getApellido() != null) user.setApellido(updatedUser.getApellido());
            if (updatedUser.getEmail() != null) {
                // Verificar que el nuevo email no esté en uso por otro usuario
                User existingUser = User.find("email = ?1 and id != ?2", updatedUser.getEmail(), id).firstResult();
                if (existingUser != null) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El email ya está en uso\"}")
                            .build();
                }
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPasswordHash() != null) user.setPasswordHash(updatedUser.getPasswordHash());
            user.setEsAdmin(updatedUser.isEsAdmin());

            user.persist();
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al actualizar usuario: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * PATCH /api/users/{id}
     * Actualiza parcialmente los datos de un usuario (solo campos no-null).
     * 
     * @param id ID del usuario
     * @param partialUser Datos a actualizar (null = no actualizar)
     * @return Usuario actualizado (200 OK) o 404 Not Found
     */
    @PATCH
    @Path("/{id}")
    public Response patchUpdateUser(@PathParam("id") int id, User partialUser) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }

            // Actualizar solo campos no-null
            if (partialUser.getNombre() != null && !partialUser.getNombre().isEmpty()) {
                user.setNombre(partialUser.getNombre());
            }
            if (partialUser.getApellido() != null && !partialUser.getApellido().isEmpty()) {
                user.setApellido(partialUser.getApellido());
            }
            if (partialUser.getEmail() != null && !partialUser.getEmail().isEmpty()) {
                // Verificar que el nuevo email no esté en uso por otro usuario
                User existingUser = User.find("email = ?1 and id != ?2", partialUser.getEmail(), id).firstResult();
                if (existingUser != null) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"El email ya está en uso\"}")
                            .build();
                }
                user.setEmail(partialUser.getEmail());
            }
            if (partialUser.getPasswordHash() != null && !partialUser.getPasswordHash().isEmpty()) {
                user.setPasswordHash(partialUser.getPasswordHash());
            }
            // Solo actualizar esAdmin si viene explícitamente (no se puede pasar null en boolean)
            // Por eso usamos PATCH solo para campos opcionales

            user.persist();
            return Response.ok(user).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error en PATCH: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * DELETE /api/users/{id}
     * Elimina un usuario del sistema.
     * 
     * @param id ID del usuario a eliminar
     * @return 204 No Content si se eliminó, 404 Not Found si no existe
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") int id) {
        try {
            User user = User.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Usuario no encontrado\"}")
                        .build();
            }

            user.delete();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al eliminar usuario: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
