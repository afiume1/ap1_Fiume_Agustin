-- ============================================================
-- Sistema de Gestion de Turnos y Consultas
-- Clinica Salud Integral S.R.L.
-- Autor: Fiume, Agustin Nicolas - VINF016173
-- ============================================================

DROP DATABASE IF EXISTS clinica_turnos;
CREATE DATABASE clinica_turnos CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE clinica_turnos;

CREATE TABLE ObraSocial (
    id_obra_social INT NOT NULL, nombre VARCHAR(100) NOT NULL, codigo VARCHAR(20) NOT NULL,
    CONSTRAINT pk_obra_social PRIMARY KEY (id_obra_social)
);

CREATE TABLE Paciente (
    id_paciente INT NOT NULL, nombre VARCHAR(60) NOT NULL, apellido VARCHAR(60) NOT NULL,
    dni VARCHAR(10) NOT NULL, fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(20), email VARCHAR(100), id_obra_social INT,
    CONSTRAINT pk_paciente PRIMARY KEY (id_paciente),
    CONSTRAINT uq_paciente_dni UNIQUE (dni),
    CONSTRAINT fk_pac_obra FOREIGN KEY (id_obra_social) REFERENCES ObraSocial(id_obra_social)
);

CREATE TABLE Especialidad (
    id_especialidad INT NOT NULL, nombre_especialidad VARCHAR(80) NOT NULL,
    CONSTRAINT pk_especialidad PRIMARY KEY (id_especialidad)
);

CREATE TABLE Profesional (
    id_profesional INT NOT NULL, nombre VARCHAR(60) NOT NULL, apellido VARCHAR(60) NOT NULL,
    matricula VARCHAR(20) NOT NULL, id_especialidad INT NOT NULL,
    CONSTRAINT pk_profesional PRIMARY KEY (id_profesional),
    CONSTRAINT uq_prof_matricula UNIQUE (matricula),
    CONSTRAINT fk_prof_especialidad FOREIGN KEY (id_especialidad) REFERENCES Especialidad(id_especialidad)
);

CREATE TABLE HorarioDisponible (
    id_horario INT NOT NULL, id_profesional INT NOT NULL,
    dia_semana VARCHAR(10) NOT NULL, hora_inicio TIME NOT NULL, hora_fin TIME NOT NULL,
    CONSTRAINT pk_horario PRIMARY KEY (id_horario),
    CONSTRAINT fk_hor_prof FOREIGN KEY (id_profesional) REFERENCES Profesional(id_profesional)
);

CREATE TABLE Turno (
    id_turno INT NOT NULL, id_paciente INT NOT NULL, id_profesional INT NOT NULL,
    fecha_hora DATETIME NOT NULL, estado VARCHAR(10) NOT NULL,
    motivo_consulta VARCHAR(200), fecha_cancelacion DATE,
    CONSTRAINT pk_turno PRIMARY KEY (id_turno),
    CONSTRAINT fk_tur_pac FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente),
    CONSTRAINT fk_tur_prof FOREIGN KEY (id_profesional) REFERENCES Profesional(id_profesional)
);

CREATE TABLE Consulta (
    id_consulta INT NOT NULL, id_turno INT NOT NULL,
    diagnostico VARCHAR(500), tratamiento VARCHAR(500),
    observaciones VARCHAR(500), fecha_consulta DATE NOT NULL,
    CONSTRAINT pk_consulta PRIMARY KEY (id_consulta),
    CONSTRAINT uq_con_turno UNIQUE (id_turno),
    CONSTRAINT fk_con_turno FOREIGN KEY (id_turno) REFERENCES Turno(id_turno)
);

CREATE TABLE Usuario (
    id_usuario INT NOT NULL, nombre_usuario VARCHAR(50) NOT NULL,
    password_hash VARCHAR(64) NOT NULL, rol VARCHAR(15) NOT NULL, id_profesional INT,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usr_nombre UNIQUE (nombre_usuario),
    CONSTRAINT fk_usr_prof FOREIGN KEY (id_profesional) REFERENCES Profesional(id_profesional)
);

-- Datos de prueba
INSERT INTO ObraSocial VALUES (1,'OSDE','OSDE-001'),(2,'Swiss Medical','SWISS-001'),(3,'PAMI','PAMI-001'),(4,'Particular','PART-000');
INSERT INTO Especialidad VALUES (1,'Clinica Medica'),(2,'Pediatria'),(3,'Cardiologia'),(4,'Dermatologia');
INSERT INTO Profesional VALUES (1,'Maria','Gonzalez','MP-12345',1),(2,'Carlos','Rodriguez','MP-67890',2),(3,'Laura','Martinez','MP-11111',3);
INSERT INTO HorarioDisponible VALUES (1,1,'Lunes','08:00:00','12:00:00'),(2,1,'Miercoles','08:00:00','12:00:00'),(3,2,'Martes','09:00:00','13:00:00');
INSERT INTO Paciente VALUES (1,'Juan','Perez','30111222','1985-03-15','1143001122','juan@mail.com',1),(2,'Ana','Lopez','27333444','1990-07-22','1155002233','ana@mail.com',2),(3,'Carlos','Fernandez','29555666','1978-11-05','1167003344','carlos@mail.com',3);
INSERT INTO Turno VALUES (1,1,1,'2025-08-04 09:00:00','Activo','Control anual',NULL),(2,2,2,'2025-08-04 10:00:00','Activo','Fiebre',NULL),(3,3,3,'2025-08-01 14:00:00','Atendido','Dolor de pecho',NULL);
INSERT INTO Consulta VALUES (1,3,'Hipertension arterial leve','Enalapril 5mg cada 12hs','Control en 30 dias','2025-08-01');
INSERT INTO Usuario VALUES (1,'admin','240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a','ADMIN',NULL);

-- Consultas SQL del TP2 seccion 13.3
-- Consulta 1: Turnos activos de un profesional por fecha
-- SELECT t.id_turno, CONCAT(p.apellido,', ',p.nombre) AS paciente, t.fecha_hora, t.estado
-- FROM Turno t JOIN Paciente p ON t.id_paciente = p.id_paciente
-- WHERE t.id_profesional = 1 AND DATE(t.fecha_hora) = '2025-08-04' AND t.estado = 'Activo'
-- ORDER BY t.fecha_hora;

-- Consulta 2: Verificacion de disponibilidad (CU-09)
-- SELECT COUNT(*) AS ocupado FROM Turno
-- WHERE id_profesional = 1 AND fecha_hora = '2025-08-04 09:00:00' AND estado = 'Activo';

-- Consulta 3: Historial de un paciente (RF05)
-- SELECT t.fecha_hora, CONCAT(pr.apellido,', ',pr.nombre) AS profesional, c.diagnostico, c.tratamiento
-- FROM Turno t JOIN Profesional pr ON t.id_profesional = pr.id_profesional
-- LEFT JOIN Consulta c ON c.id_turno = t.id_turno
-- WHERE t.id_paciente = 1 ORDER BY t.fecha_hora DESC;

-- Borrado logico
-- UPDATE Turno SET estado = 'Cancelado', fecha_cancelacion = CURDATE() WHERE id_turno = 1;

-- Borrado fisico (solo mantenimiento)
-- DELETE FROM Consulta WHERE id_turno = 1;
-- DELETE FROM Turno WHERE id_turno = 1;
