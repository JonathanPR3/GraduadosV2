-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-02-2025 a las 20:59:11
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: graduacion
--
create database graduacion;
use graduacion;
-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla admins
--

CREATE TABLE admins (
  id int(11) NOT NULL,
  curp varchar(18) NOT NULL,
  nombre varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla graduados
--

CREATE TABLE graduados (
  id int(11) NOT NULL,
  curp varchar(18) NOT NULL,
  nombre varchar(100) NOT NULL,
  asistencia tinyint(1) NOT NULL DEFAULT 0,
  op_titulacion varchar(50) NOT NULL,
  asiento varchar(10) DEFAULT NULL,
  acompanantes tinyint(1) NOT NULL DEFAULT 0,
  carrera varchar(100) NOT NULL,
  grupo varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla admins
--
ALTER TABLE admins
  ADD PRIMARY KEY (id);

--
-- Indices de la tabla graduados
--
ALTER TABLE graduados
  ADD PRIMARY KEY (id);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla admins
--
ALTER TABLE admins
  MODIFY id int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla graduados

ALTER TABLE graduados
  MODIFY id int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */