package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para consumir el API REST de usuarios en http://localhost:8081/api/users
 * 
 * @author Erick
 */
public class UserApiService {
    
    private static final String API_BASE_URL = "http://localhost:8081/api/users";
    private final Gson gson;
    
    public UserApiService() {
        this.gson = new Gson();
    }
    
    /**
     * Obtiene la lista completa de usuarios desde el API REST.
     * 
     * @return Lista de usuarios
     * @throws Exception Si hay error en la comunicación con el API
     */
    public List<User> listarUsuarios() throws Exception {
        try {
            URL url = new URL(API_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Convertir JSON a lista de usuarios
                List<UserApiDto> apiUsers = gson.fromJson(response.toString(), 
                        new TypeToken<List<UserApiDto>>(){}.getType());
                
                // Convertir DTO a modelo User
                List<User> users = new ArrayList<>();
                for (UserApiDto apiUser : apiUsers) {
                    User user = new User();
                    user.setId(apiUser.getId());
                    user.setNombre(apiUser.getNombre());
                    user.setApellido(apiUser.getApellido());
                    user.setEmail(apiUser.getEmail());
                    user.setPassword(apiUser.getPasswordHash());
                    user.setRol(apiUser.isEsAdmin());
                    user.setFechaRegistro(apiUser.getFechaRegistro());
                    users.add(user);
                }
                
                return users;
            } else {
                throw new Exception("Error al obtener usuarios: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene un usuario específico por su ID desde el API REST.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado o null
     * @throws Exception Si hay error en la comunicación
     */
    public User obtenerUsuarioPorId(int id) throws Exception {
        try {
            URL url = new URL(API_BASE_URL + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                UserApiDto apiUser = gson.fromJson(response.toString(), UserApiDto.class);
                
                User user = new User();
                user.setId(apiUser.getId());
                user.setNombre(apiUser.getNombre());
                user.setApellido(apiUser.getApellido());
                user.setEmail(apiUser.getEmail());
                user.setPassword(apiUser.getPasswordHash());
                user.setRol(apiUser.isEsAdmin());
                user.setFechaRegistro(apiUser.getFechaRegistro());
                
                return user;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            } else {
                throw new Exception("Error al obtener usuario: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Promueve un usuario a administrador mediante el API REST.
     * 
     * @param userId ID del usuario a promover
     * @throws Exception Si hay error en la operación
     */
    public void promoteToAdmin(int userId) throws Exception {
        try {
            URL url = new URL(API_BASE_URL + "/" + userId + "/promote");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Usar POST en lugar de PATCH para evitar problemas con el sistema de módulos de Java
            conn.setRequestMethod("POST");
            
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error al promover usuario: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte un administrador en usuario cliente mediante el API REST.
     * 
     * @param userId ID del usuario a convertir
     * @throws Exception Si hay error en la operación
     */
    public void demoteToClient(int userId) throws Exception {
        try {
            URL url = new URL(API_BASE_URL + "/" + userId + "/demote");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Usar POST en lugar de PATCH para evitar problemas con el sistema de módulos de Java
            conn.setRequestMethod("POST");
            
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error al convertir usuario a cliente: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea un nuevo usuario mediante el API REST.
     * 
     * @param user Usuario a crear
     * @throws Exception Si hay error en la operación
     */
    public void agregarUsuario(User user) throws Exception {
        try {
            URL url = new URL(API_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Crear JSON del usuario
            UserApiDto apiUser = new UserApiDto();
            apiUser.setNombre(user.getNombre());
            apiUser.setApellido(user.getApellido());
            apiUser.setEmail(user.getEmail());
            // Hashear la contraseña antes de enviarla
            apiUser.setPasswordHash(hashPassword(user.getPassword()));
            apiUser.setEsAdmin(user.isRol());
            
            String jsonInputString = gson.toJson(apiUser);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                throw new Exception("Error al crear usuario: HTTP " + responseCode + " - " + response.toString());
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualiza un usuario existente mediante el API REST.
     * 
     * @param user Usuario con datos actualizados
     * @throws Exception Si hay error en la operación
     */
    public void modificarUsuario(User user) throws Exception {
        try {
            URL url = new URL(API_BASE_URL + "/" + user.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Crear JSON del usuario
            UserApiDto apiUser = new UserApiDto();
            apiUser.setId(user.getId());
            apiUser.setNombre(user.getNombre());
            apiUser.setApellido(user.getApellido());
            apiUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Hashear la contraseña antes de enviarla
                apiUser.setPasswordHash(hashPassword(user.getPassword()));
            }
            apiUser.setEsAdmin(user.isRol());
            
            String jsonInputString = gson.toJson(apiUser);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Error al actualizar usuario: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Autentica un usuario con email y contraseña mediante el API REST.
     * 
     * @param email Email del usuario
     * @param password Contraseña (será hasheada antes de enviar)
     * @return Usuario autenticado o null si las credenciales son inválidas
     * @throws Exception Si hay error en la comunicación
     */
    public User autenticarUsuario(String email, String password) throws Exception {
        try {
            // Hashear la contraseña usando SHA-256
            String hashedPassword = hashPassword(password);
            
            URL url = new URL(API_BASE_URL + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Crear JSON con credenciales
            String jsonInputString = String.format("{\"email\":\"%s\",\"passwordHash\":\"%s\"}", 
                    email, hashedPassword);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                UserApiDto apiUser = gson.fromJson(response.toString(), UserApiDto.class);
                
                User user = new User();
                user.setId(apiUser.getId());
                user.setNombre(apiUser.getNombre());
                user.setApellido(apiUser.getApellido());
                user.setEmail(apiUser.getEmail());
                user.setPassword(apiUser.getPasswordHash());
                user.setRol(apiUser.isEsAdmin());
                user.setFechaRegistro(apiUser.getFechaRegistro());
                
                return user;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return null; // Credenciales inválidas
            } else {
                throw new Exception("Error al autenticar usuario: HTTP " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error al comunicarse con el API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Hashea una contraseña usando SHA-256.
     * 
     * @param password Contraseña en texto plano
     * @return Contraseña hasheada en hexadecimal
     */
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }
    
    /**
     * DTO para mapear la respuesta JSON del API REST.
     */
    private static class UserApiDto {
        private int id;
        private String nombre;
        private String apellido;
        private String email;
        private String passwordHash;
        private boolean esAdmin;
        private String fechaRegistro;
        
        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPasswordHash() { return passwordHash; }
        public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
        
        public boolean isEsAdmin() { return esAdmin; }
        public void setEsAdmin(boolean esAdmin) { this.esAdmin = esAdmin; }
        
        public String getFechaRegistro() { return fechaRegistro; }
        public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    }
}
