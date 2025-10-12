-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-10-2025 a las 03:13:49
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `enroute_aqp_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_viajes`
--

CREATE TABLE `historial_viajes` (
  `id_viaje` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `origen` varchar(200) DEFAULT NULL,
  `destino` varchar(200) DEFAULT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lineas`
--

CREATE TABLE `lineas` (
  `id_linea` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `linea_paraderos`
--

CREATE TABLE `linea_paraderos` (
  `id_linea_paradero` int(11) NOT NULL,
  `id_linea` int(11) NOT NULL,
  `id_paradero` int(11) NOT NULL,
  `orden` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paraderos`
--

CREATE TABLE `paraderos` (
  `id_paradero` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `latitud` decimal(10,8) NOT NULL,
  `longitud` decimal(11,8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rutas`
--

CREATE TABLE `rutas` (
  `id` int(11) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `origen` varchar(150) NOT NULL,
  `destino` varchar(150) NOT NULL,
  `paraderos` text DEFAULT NULL,
  `empresa` varchar(100) DEFAULT NULL,
  `tipo` enum('Troncal','Estructurante','Alimentadora') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rutas`
--

INSERT INTO `rutas` (`id`, `codigo`, `nombre`, `origen`, `destino`, `paraderos`, `empresa`, `tipo`) VALUES
(1, 'BT1', 'Troncal Norte-Sur', 'Terminal Pesquero (Río Seco, Cerro Colorado)', 'Estación Sur Lara (Socabaya)', 'Fuerte Coronel Bolognesi, Plaza de Río Seco, Zamácola, Complejo de Prestaciones Sociales EsSalud, Campo Verde, Urb. La Fonda, Metro, Cerro Viejo, Centro de Salud Mental Moisés Heresi, Circunvalación, Real Plaza, Mall Plaza, Quesada, Recoleta, Puente Grau, Moral, Plaza de Armas, Consuelo, La Merced, Quiroz, Independencia, Hospital General Honorio Delgado Espinoza, ADEPA, Parroquia Santo Toribio de Mogrovejo, Andrés Avelino Cáceres, La Isla, La Apacheta, 13 de Enero, Los Toritos, Centro de Salud San Martín de Socabaya, Mercado San Martín de Socabaya, Grifo San Fernando, Coscollo, Parque a la Madre - El Triángulo, Urb. Lara', 'Integra Arequipa S.A.C.', 'Troncal'),
(2, 'BT2', 'Troncal Sur-Norte', 'Estación Sur Lara (Socabaya)', 'Terminal Pesquero (Río Seco, Cerro Colorado)', 'Parque a la Madre - El Triángulo, Coscollo, Grifo San Fernando - Repsol, Mercado San Martín de Socabaya, Centro de Salud San Martín de Socabaya, Aplao, 13 de Enero, La Apacheta, La Isla, Monumento a los Bomberos, Los Pinos, ADEPA, Hospital General Honorio Delgado Espinoza, Estadio Melgar, Garcí de Carbajal, Víctor Lira, Paucarpata, La Salle, Manuel Muñoz Nájar, Puno, Plaza Mayta Cápac, San Martín, Calvario, Taboada, Primavera, Peral, San Lázaro, Puente Grau, Misti, Emmel, Mall Plaza, Real Plaza, Circunvalación, Socosani, Cerro Viejo, Metro, Urb. La Fonda, Campo Verde, Colegio Vencedor, Zamácola, Plaza de Río Seco', 'Integra Arequipa S.A.C.', 'Troncal'),
(3, 'T1', '', 'Terminal Bus Characato (Juventud Characato)', 'Mercado Pesquero El Palomar (Cercado)', '', 'Bus Characato S.A.', 'Troncal'),
(4, 'T2', '', 'Horacio Zeballos G. (Socabaya)', 'Seguro Social (Cercado)', '', '', 'Troncal'),
(5, 'T3', '', 'San Bernardo de Chiguata (Paucarpata)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(6, 'T4', '', 'San Bernardo de Chiguata (Paucarpata)', 'El Palomar (Cercado)', '', '', 'Troncal'),
(7, 'T5', '', 'Posada de Cristo (Paucarpata)', 'El Palomar (Cercado)', '', '', 'Troncal'),
(8, 'T6', '', 'Cerrito Huacsapata (Paucarpata)', 'Seguro Social (Cercado)', '', '', 'Troncal'),
(9, 'T7', '', 'Los Olivos (Mariano Melgar)', 'Estadio Melgar IV Centenario (Cercado)', '', '', 'Troncal'),
(10, 'T8', '', 'Alto San Martín (Mariano Melgar)', 'La Paz (Cercado)', '', '', 'Troncal'),
(11, 'T9', '', 'Alto Jesús (Paucarpata)', 'Seguro Social (Cercado)', '', '', 'Troncal'),
(12, 'T11', '', 'San Bernardo de Chiguata (Paucarpata)', '28 de Julio (Cercado)', '', '', 'Troncal'),
(13, 'T12', '', 'Cahuaya Rosaspata (Paucarpata)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(14, 'T13', '', 'La Galaxia (Miraflores)', 'La Merced (Cercado)', '', '', 'Troncal'),
(15, 'T14', '', 'Tomasa Tito Condemayta (Miraflores)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(16, 'T15', '', 'San Luis (Alto Selva Alegre)', 'La Merced (Cercado)', '', '', 'Troncal'),
(17, 'T16', '', 'San Luis (Alto Selva Alegre)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(18, 'T17', '', 'Señor de las Piedades (Alto Selva Alegre)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(19, 'T18', '', 'Huarangal (Alto Selva Alegre)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(20, 'T19', '', 'Parque Acuático de Tingo (Cercado)', 'Plaza Las Américas (Cerro Colorado)', '', '', 'Troncal'),
(21, 'T20', '', 'Villa El Triunfo (Sachaca)', 'Las Begonias (J.L.B.Y.R.)', '', '', 'Troncal'),
(22, 'T21', '', 'Centro de Salud Tiabaya (Tiabaya)', 'La Merced (Cercado)', '', '', 'Troncal'),
(23, 'T22', '', 'Urb. Betania (J.L.B.Y.R.)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(24, 'T23', '', 'La Campiña (Socabaya)', 'La Paz (Cercado)', '', '', 'Troncal'),
(25, 'T24', '', 'Pueblo Tradicional de Uchumayo (Uchumayo)', 'Quiroz (Cercado)', '', '', 'Troncal'),
(26, 'T25', '', 'Pueblo Tradicional de Uchumayo (Uchumayo)', 'Gobierno Regional de Arequipa (Paucarpata)', '', '', 'Troncal'),
(27, 'T26', '', 'Pampas Nuevas (Tiabaya)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(28, 'T28', '', 'Congata (Uchumayo)', 'La Merced (Cercado)', '', '', 'Troncal'),
(29, 'T29', '', 'Pampas del Cusco (Hunter)', 'La Merced (Cercado)', '', '', 'Troncal'),
(30, 'T30', '', 'Villa Sevilla (Hunter)', 'La Merced (Cercado)', '', '', 'Troncal'),
(31, 'T31', '', 'Pampas del Cusco (Hunter)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(32, 'T32', '', 'Miguel Grau (Paucarpata)', 'Juventud Ferroviaria (Cercado)', '', '', 'Troncal'),
(33, 'T33', '', 'San Bernardo de Chiguata (Paucarpata)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(34, 'T35', '', 'Héroes del Cenepa (Mariano Melgar)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(35, 'T36', '', '4 de Octubre (Socabaya)', 'Seguro Social (Cercado)', '', '', 'Troncal'),
(36, 'T37', '', 'Ciudad Blanca (Paucarpata)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(37, 'T38', '', 'Villa Confraternidad (Alto Selva Alegre)', 'Mercado Mayorista Los Incas (J.L.B.Y.R.)', '', '', 'Troncal'),
(38, 'A1', '', 'Alto Libertad (Cerro Colorado)', 'Álvarez Thomas (Cercado)', '', '', 'Troncal'),
(39, 'A2', '', 'Alto Libertad (Cerro Colorado)', 'Facultad de Medicina - UNSA (Cercado)', '', '', 'Troncal'),
(40, 'A3', '', 'Villa Hermosa (Cerro Colorado)', 'Puente Grau (Cercado)', '', '', 'Troncal'),
(41, 'A4', '', 'Pampa de Camarones (Sachaca)', 'SENCICO (Yanahuara)', '', '', 'Troncal'),
(42, 'A5', '', '1 de Junio - Zona \"B\" (Cayma)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(43, 'A6', '', 'Cerrillo (Characato)', 'Cementerio La Apacheta (J.L.B.Y.R.)', '', '', 'Troncal'),
(44, 'A7', '', 'Los Portales (Paucarpata)', 'Cementerio La Apacheta (J.L.B.Y.R.)', '', '', 'Troncal'),
(45, 'A8', '', 'Pachacútec (Cerro Colorado)', 'Villa Hermosa (Cerro Colorado)', '', '', 'Troncal'),
(46, 'A9', '', 'Pachacútec (Cerro Colorado)', 'La Libertad (Cerro Colorado)', '', '', 'Troncal'),
(47, 'A10', '', 'Villa Santa Rosa (Paucarpata)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(48, 'A11', '', 'Horacio Zeballos G. (Socabaya)', 'Estación Sur Lara (Socabaya)', '', '', 'Troncal'),
(49, 'A12', '', 'La Campiña (Socabaya)', 'Estación Sur Lara (Socabaya)', '', '', 'Troncal'),
(50, 'A13', '', 'Ampliación Socabaya (Socabaya)', 'Tasahuayo (Socabaya)', '', '', 'Troncal'),
(51, 'A14', '', 'El Mirador (Alto Selva Alegre)', 'La Salle (Cercado)', '', '', 'Troncal'),
(52, 'A15', '', 'Cementerio Municipal (Miraflores)', 'Seguro Social (Cercado)', '', '', 'Troncal'),
(53, 'A17', '', 'Alto Libertad (Cerro Colorado)', 'Grifo Monterrey (J.L.B.Y.R.)', '', '', 'Troncal'),
(54, 'A18', '', 'El Pasto (Socabaya)', 'Urb. Dolores (J.L.B.Y.R.)', '', '', 'Troncal'),
(55, 'A19', '', 'Ampliación Pampas del Cusco (Hunter)', 'Plataforma Andrés A. Cáceres (J.L.B.Y.R.)', '', '', 'Troncal'),
(56, 'A20', '', 'La Planicie (Sachaca)', 'Agricultura (J.L.B.Y.R.)', '', '', 'Troncal'),
(57, 'A21', '', 'Peregrinos (Yarabamba)', 'Terminal Terrestre (J.L.B.Y.R. / Hunter / Cercado)', '', '', 'Troncal'),
(58, 'A22', '', 'La Mansión (Socabaya)', 'Estación Sur Lara (Socabaya)', '', '', 'Troncal'),
(59, 'A24', '', 'Pachacútec (Cerro Colorado)', 'Metro (Cerro Colorado)', '', '', 'Troncal'),
(60, 'A25', '', 'PERUARBO (Cerro Colorado)', 'Estación Norte Río Seco (Cerro Colorado)', '', '', 'Troncal'),
(61, 'A26', '', '1 de Junio - Zona \"B\" (Cayma)', 'Puente Bolognesi (Cercado)', '', '', 'Troncal'),
(62, 'A27', '', 'José Luis Bustamante y Rivero (Cerro Colorado)', 'Estación Norte Río Seco (Cerro Colorado)', '', '', 'Troncal'),
(63, 'A28', '', 'Milagros (Yura)', 'Ala Aérea N° 3 (Cerro Colorado)', '', '', 'Troncal'),
(64, 'A29', '', 'Sor Ana de los Ángeles (Cerro Colorado)', 'Estación Norte Río Seco (Cerro Colorado)', '', '', 'Troncal'),
(65, 'A30', '', 'Los Ángeles del Sur (Cerro Colorado)', 'Estación Norte Río Seco (Cerro Colorado)', '', '', 'Troncal'),
(66, 'A31', '', 'Mujeres con Esperanza (Cayma)', 'Zamácola (Cerro Colorado)', '', '', 'Troncal'),
(67, 'A32', '', 'Jardines de Chachani (Cerro Colorado)', 'Zamácola (Cerro Colorado)', '', '', 'Troncal'),
(68, 'A33', '', 'Las Flores (Cerro Colorado)', 'Zamácola (Cerro Colorado)', '', '', 'Troncal'),
(69, 'A34', '', 'Mujeres con Esperanza (Cayma)', 'Plataforma Andrés A. Cáceres (J.L.B.Y.R.)', '', '', 'Troncal'),
(70, 'A35', '', 'Betania (J.L.B.Y.R.)', 'Parque Pablo VI (Cercado)', '', '', 'Troncal'),
(71, 'A36', '', 'Los Pioneros (Cayma)', 'Quesada (Yanahuara)', '', '', 'Troncal'),
(72, 'A37', '', 'Ciudad Municipal (Cerro Colorado)', 'Estación Norte Río Seco (Cerro Colorado)', '', '', 'Troncal'),
(73, 'A38', '', 'Milagros (Yura)', 'Plataforma Andrés A. Cáceres (J.L.B.Y.R.)', '', '', 'Troncal'),
(74, 'A39', '', 'Los Pioneros (Cayma)', 'Zamácola (Cerro Colorado)', '', '', 'Troncal'),
(75, 'A40', '', 'Javier Heraud (Alto Selva Alegre)', 'La Paz (Cercado)', '', '', 'Troncal'),
(76, 'A41', '', 'Villa Ecológica (Alto Selva Alegre)', 'Goyeneche (Cercado)', '', '', 'Troncal'),
(77, 'A42', '', 'UPIS Paisajista (Hunter)', 'Álvarez Thomas (Cercado)', '', '', 'Troncal'),
(78, 'A43', '', 'Berlín (Mariano Melgar)', 'La Paz (Cercado)', '', '', 'Troncal'),
(79, 'A44', '', 'Los Cristales (Socabaya)', 'Parque Lambramani (Cercado)', '', '', 'Troncal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rutas_alternativas`
--

CREATE TABLE `rutas_alternativas` (
  `id_ruta` int(11) NOT NULL,
  `id_linea_origen` int(11) NOT NULL,
  `id_paradero_transbordo` int(11) NOT NULL,
  `id_linea_destino` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre`, `apellido`, `email`, `password_hash`, `fecha_registro`) VALUES
(1, 'Erick', 'Saldivar', 'abc@mail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2025-10-11 22:54:27');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `historial_viajes`
--
ALTER TABLE `historial_viajes`
  ADD PRIMARY KEY (`id_viaje`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `lineas`
--
ALTER TABLE `lineas`
  ADD PRIMARY KEY (`id_linea`);

--
-- Indices de la tabla `linea_paraderos`
--
ALTER TABLE `linea_paraderos`
  ADD PRIMARY KEY (`id_linea_paradero`),
  ADD KEY `id_linea` (`id_linea`),
  ADD KEY `id_paradero` (`id_paradero`);

--
-- Indices de la tabla `paraderos`
--
ALTER TABLE `paraderos`
  ADD PRIMARY KEY (`id_paradero`);

--
-- Indices de la tabla `rutas`
--
ALTER TABLE `rutas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `rutas_alternativas`
--
ALTER TABLE `rutas_alternativas`
  ADD PRIMARY KEY (`id_ruta`),
  ADD KEY `id_linea_origen` (`id_linea_origen`),
  ADD KEY `id_paradero_transbordo` (`id_paradero_transbordo`),
  ADD KEY `id_linea_destino` (`id_linea_destino`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `historial_viajes`
--
ALTER TABLE `historial_viajes`
  MODIFY `id_viaje` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `lineas`
--
ALTER TABLE `lineas`
  MODIFY `id_linea` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `linea_paraderos`
--
ALTER TABLE `linea_paraderos`
  MODIFY `id_linea_paradero` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `paraderos`
--
ALTER TABLE `paraderos`
  MODIFY `id_paradero` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rutas`
--
ALTER TABLE `rutas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;

--
-- AUTO_INCREMENT de la tabla `rutas_alternativas`
--
ALTER TABLE `rutas_alternativas`
  MODIFY `id_ruta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `historial_viajes`
--
ALTER TABLE `historial_viajes`
  ADD CONSTRAINT `historial_viajes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `linea_paraderos`
--
ALTER TABLE `linea_paraderos`
  ADD CONSTRAINT `linea_paraderos_ibfk_1` FOREIGN KEY (`id_linea`) REFERENCES `lineas` (`id_linea`),
  ADD CONSTRAINT `linea_paraderos_ibfk_2` FOREIGN KEY (`id_paradero`) REFERENCES `paraderos` (`id_paradero`);

--
-- Filtros para la tabla `rutas_alternativas`
--
ALTER TABLE `rutas_alternativas`
  ADD CONSTRAINT `rutas_alternativas_ibfk_1` FOREIGN KEY (`id_linea_origen`) REFERENCES `lineas` (`id_linea`),
  ADD CONSTRAINT `rutas_alternativas_ibfk_2` FOREIGN KEY (`id_paradero_transbordo`) REFERENCES `paraderos` (`id_paradero`),
  ADD CONSTRAINT `rutas_alternativas_ibfk_3` FOREIGN KEY (`id_linea_destino`) REFERENCES `lineas` (`id_linea`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
