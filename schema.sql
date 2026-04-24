-- ============================================================
-- Sistema de Gestión de Turnos y Consultas
-- Clínica Salud Integral S.R.L.
-- Autor: Fiume, Agustín - VINF016173
-- ============================================================

DROP DATABASE IF EXISTS clinica_turnos;
CREATE DATABASE clinica_turnos CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE clinica_turnos;

-- ------------------------------------------------------------
-- Obras sociales
-- ------------------------------------------------------------
CREATE TABLE ObraSocial (
    id_obra_social INT NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    codigo         VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_obra_social PRIMARY KEY (id_obra_social)
);

-- ------------------------------------------------------------
-- Pacientes
-- ------------------------------------------------------------
CREATE TABLE Paciente (
    id_paciente      INT          NOT NULL,
    nombre           VARCHAR(60)  NOT NULL,
    apellido         VARCHAR(60)  NOT NULL,
    dni              VARCHAR(10)  NOT NULL,
    fecha_nacimiento DATE         NOT NULL,
    telefono         VARCHAR(20),
    email            VARCHAR(100),
    id_obra_social   INT,
    CONSTRAINT pk_paciente       PRIMARY KEY (id_paciente),
    CONSTRAINT uq_paciente_dni   UNIQUE (dni),
    CONSTRAINT fk_pac_obra       FOREIGN KEY (id_obra_social)
        REFERENCES ObraSocial(id_obra_social)
);

-- ------------------------------------------------------------
-- Especialidades médicas
-- ------------------------------------------------------------
CREATE TABLE Especialidad (
    id_especialidad    INT         NOT NULL,
    nombre_especialidad VARCHAR(80) NOT NULL,
    CONSTRAINT pk_especialidad PRIMARY KEY (id_especialidad)
);

-- ------------------------------------------------------------
-- Profesionales
-- ------------------------------------------------------------
CREATE TABLE Profesional (
    id_profesional  INT         NOT NULL,
    nombre          VARCHAR(60) NOT NULL,
    apellido        VARCHAR(60) NOT NULL,
    matricula       VARCHAR(20) NOT NULL,
    id_especialidad INT         NOT NULL,
    CONSTRAINT pk_profesional      PRIMARY KEY (id_profesional),
    CONSTRAINT uq_prof_matricula   UNIQUE (matricula),
    CONSTRAINT fk_prof_especialidad FOREIGN KEY (id_especialidad)
        REFERENCES Especialidad(id_especialidad)
);

-- ------------------------------------------------------------
-- Horarios disponibles de cada profesional
-- ------------------------------------------------------------
CREATE TABLE HorarioDisponible (
    id_horario     INT        NOT NULL,
    id_profesional INT        NOT NULL,
    dia_semana     VARCHAR(10) NOT NULL CHECK (dia_semana IN ('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado')),
    hora_inicio    TIME       NOT NULL,
    hora_fin       TIME       NOT NULL,
    CONSTRAINT pk_horario      PRIMARY KEY (id_horario),
    CONSTRAINT fk_hor_prof     FOREIGN KEY (id_profesional)
        REFERENCES Profesional(id_profesional)
);

-- ------------------------------------------------------------
-- Turnos
-- ------------------------------------------------------------
CREATE TABLE Turno (
    id_turno         INT          NOT NULL,
    id_paciente      INT          NOT NULL,
    id_profesional   INT          NOT NULL,
    fecha_hora       DATETIME     NOT NULL,
    estado           VARCHAR(10)  NOT NULL CHECK (estado IN ('Activo','Atendido','Cancelado')),
    motivo_consulta  VARCHAR(200),
    fecha_cancelacion DATE,
    CONSTRAINT pk_turno       PRIMARY KEY (id_turno),
    CONSTRAINT fk_tur_pac     FOREIGN KEY (id_paciente)
        REFERENCES Paciente(id_paciente),
    CONSTRAINT fk_tur_prof    FOREIGN KEY (id_profesional)
        REFERENCES Profesional(id_profesional)
);

-- ------------------------------------------------------------
-- Consultas (registro clínico)
-- ------------------------------------------------------------
CREATE TABLE Consulta (
    id_consulta    INT          NOT NULL,
    id_turno       INT          NOT NULL,
    diagnostico    VARCHAR(500),
    tratamiento    VARCHAR(500),
    observaciones  VARCHAR(500),
    fecha_consulta DATE         NOT NULL,
    CONSTRAINT pk_consulta   PRIMARY KEY (id_consulta),
    CONSTRAINT uq_con_turno  UNIQUE (id_turno),
    CONSTRAINT fk_con_turno  FOREIGN KEY (id_turno)
        REFERENCES Turno(id_turno)
);

-- ------------------------------------------------------------
-- Usuarios del sistema
-- ------------------------------------------------------------
CREATE TABLE Usuario (
    id_usuario     INT         NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL,
    password_hash  VARCHAR(64) NOT NULL,
    rol            VARCHAR(15) NOT NULL CHECK (rol IN ('ADMIN','RECEPCIONISTA','MEDICO')),
    id_profesional INT,
    CONSTRAINT pk_usuario      PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usr_nombre   UNIQUE (nombre_usuario),
    CONSTRAINT fk_usr_prof     FOREIGN KEY (id_profesional)
        REFERENCES Profesional(id_profesional)
);

-- ============================================================
-- Datos de prueba
-- ============================================================

INSERT INTO ObraSocial VALUES (1, 'OSDE',        'OSDE-001');
INSERT INTO ObraSocial VALUES (2, 'Swiss Medical','SWISS-001');
INSERT INTO ObraSocial VALUES (3, 'PAMI',         'PAMI-001');
INSERT INTO ObraSocial VALUES (4, 'Particular',   'PART-000');

INSERT INTO Especialidad VALUES (1, 'Clínica Médica');
INSERT INTO Especialidad VALUES (2, 'Pediatría');
INSERT INTO Especialidad VALUES (3, 'Cardiología');
INSERT INTO Especialidad VALUES (4, 'Dermatología');

INSERT INTO Profesional VALUES (1, 'María',   'González', 'MP-12345', 1);
INSERT INTO Profesional VALUES (2, 'Carlos',  'Rodríguez','MP-67890', 2);
INSERT INTO Profesional VALUES (3, 'Laura',   'Martínez', 'MP-11111', 3);

INSERT INTO HorarioDisponible VALUES (1, 1, 'Lunes',    '08:00:00', '12:00:00');
INSERT INTO HorarioDisponible VALUES (2, 1, 'Miércoles','08:00:00', '12:00:00');
INSERT INTO HorarioDisponible VALUES (3, 2, 'Martes',   '09:00:00', '13:00:00');
INSERT INTO HorarioDisponible VALUES (4, 2, 'Jueves',   '09:00:00', '13:00:00');
INSERT INTO HorarioDisponible VALUES (5, 3, 'Viernes',  '14:00:00', '18:00:00');

INSERT INTO Paciente VALUES (1, 'Juan',    'Pérez',   '30111222', '1985-03-15', '1143001122', 'juan.perez@mail.com',   1);
INSERT INTO Paciente VALUES (2, 'Ana',     'López',   '27333444', '1990-07-22', '1155002233', 'ana.lopez@mail.com',    2);
INSERT INTO Paciente VALUES (3, 'Roberto', 'Sánchez', '20555666', '1972-11-08', '1167003344', 'roberto.s@mail.com',   3);

-- password = "admin123" (SHA-256)
INSERT INTO Usuario VALUES (1, 'admin',        'eec2d1ac3e2b...hash_admin',        'ADMIN',         NULL);
INSERT INTO Usuario VALUES (2, 'recepcion1',   'hash_recepcion1',                  'RECEPCIONISTA', NULL);
INSERT INTO Usuario VALUES (3, 'dr.gonzalez',  'hash_dr_gonzalez',                 'MEDICO',        1);
INSERT INTO Usuario VALUES (4, 'dr.rodriguez', 'hash_dr_rodriguez',                'MEDICO',        2);
