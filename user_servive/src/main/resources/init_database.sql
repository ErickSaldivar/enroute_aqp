-- Script para crear la base de datos y tabla de usuarios
-- Ejecutar en MySQL con usuario root

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS enroute_usuarios_db;

-- Usar la base de datos
USE enroute_usuarios_db;

-- Crear la tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  es_admin TINYINT(1) NOT NULL DEFAULT 0,
  fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Insertar datos de prueba (opcional)
INSERT INTO usuarios (nombre, apellido, email, password_hash, es_admin, fecha_registro) VALUES
('Admin', 'Test', 'admin@test.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1, NOW());

-- Mostrar la tabla creada
DESCRIBE usuarios;
