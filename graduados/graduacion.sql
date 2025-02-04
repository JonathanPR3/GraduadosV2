-- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS graduacion;
USE graduacion;

-- 2. Crear la tabla 'carreras'
CREATE TABLE IF NOT EXISTS carreras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Crear la tabla 'turnos'
CREATE TABLE IF NOT EXISTS turnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

-- 4. Crear la tabla 'opciones_titulacion'
CREATE TABLE IF NOT EXISTS opciones_titulacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- 5. Crear la tabla 'grupos' (Asocia carreras con turnos y define grupos específicos)
CREATE TABLE IF NOT EXISTS grupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,  -- Ejemplo: "6CM421", "6CV54321"
    id_carrera INT NOT NULL,
    id_turno INT NOT NULL,
    FOREIGN KEY (id_carrera) REFERENCES carreras(id),
    FOREIGN KEY (id_turno) REFERENCES turnos(id)
);

-- 6. Crear la tabla 'graduados'
CREATE TABLE IF NOT EXISTS graduados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    curp VARCHAR(18) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    asistencia BOOLEAN NOT NULL DEFAULT 0,
    id_opcion_titulacion INT NOT NULL,
    asiento VARCHAR(10),
    acompanantes TINYINT(1) NOT NULL DEFAULT 0,
    id_grupo INT NOT NULL,  -- Ahora cada graduado pertenece a un grupo, no solo a una carrera y turno
    FOREIGN KEY (id_opcion_titulacion) REFERENCES opciones_titulacion(id),
    FOREIGN KEY (id_grupo) REFERENCES grupos(id)
);


-- 7. Crear la tabla 'admins'
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    curp VARCHAR(18) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL
);

-- Inserts



-- Opciones de titulación, van ordenadas segun importancia, tesis son los mas importantes y escolaridad los menos.
INSERT INTO opciones_titulacion (nombre) VALUES ('TESIS'), ('PROYECTO DE INVETSIGACIÓN'), ('CURRICULAR'), ('ESCOLARIDAD');

-- Carreras, van ordenadas segun la fecha que tentativamente nos dieron, es decir primero es la ceremonia de maquinas, luego de dibujo etc...
INSERT INTO carreras (nombre) VALUES ('MÁQUINAS CON SISTEMAS AUTOMATIZADOS'), ('DIBUJO ASISTIDO POR COMPUTADORA'), ('METALURGIA'), ('DISEÑO GRÁFICO DIGITAL'), ('SISTEMAS AUTOMOTRICES'), ('AERONÁUTICA'), ('MECATRÓNICA');

-- Turnos, esta parte esta por definir, vi que en cada turno hay un grupo de una carrera, entonces hay que hacer una modificacion para por ejemplo el grupo 6cm421 es el grupo del turno matutino de la carrera x, mientras que el 6cv54321 es el de turno vespertino para la misma carrera,
INSERT INTO turnos (nombre) VALUES ('Matutino'), ('Vespertino');

-- Grupos por carrera y turno
INSERT INTO grupos (nombre, id_carrera, id_turno) VALUES 
    ('6CM421', 1, 1),  -- Máquinas con Sistemas Automatizados - Matutino
    ('6CV54321', 1, 2), -- Máquinas con Sistemas Automatizados - Vespertino
    ('6CM422', 2, 1),  -- Dibujo Asistido - Matutino
    ('6CV54322', 2, 2), -- Dibujo Asistido - Vespertino
    ('6CM423', 3, 1),  -- Metalurgia - Matutino
    ('6CV54323', 3, 2), -- Metalurgia - Vespertino
    ('6CM424', 4, 1),  -- Diseño Gráfico - Matutino
    ('6CV54324', 4, 2), -- Diseño Gráfico - Vespertino
    ('6CM425', 5, 1),  -- Sistemas Automotrices - Matutino
    ('6CV54325', 5, 2), -- Sistemas Automotrices - Vespertino
    ('6CM426', 6, 1),  -- Aeronáutica - Matutino
    ('6CV54326', 6, 2), -- Aeronáutica - Vespertino
    ('6CM427', 7, 1),  -- Mecatrónica - Matutino
    ('6CV54327', 7, 2); -- Mecatrónica - Vespertino

-- Insertar un graduado de prueba
select *from graduados;

INSERT INTO graduados (curp, nombre, asistencia, id_opcion_titulacion, asiento, acompanantes, id_grupo)
VALUES ('URP12345678912345', 'Juan Pérez', 0, 2, 'A12', 0, 1);

-- Insertar un administrador de prueba
INSERT INTO admins (curp, nombre) VALUES ('URP12345678912346', 'Jonathan');


SELECT g.id, g.nombre, g.curp, g.asistencia, g.asiento, g.acompanantes, 
       c.nombre AS carrera, t.nombre AS turno, grp.nombre AS grupo
FROM graduados g
JOIN grupos grp ON g.id_grupo = grp.id
JOIN carreras c ON grp.id_carrera = c.id
JOIN turnos t ON grp.id_turno = t.id;

