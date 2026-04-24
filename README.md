# Sistema de Gestión de Turnos y Consultas
### Actividad Práctica N.° 1 — Análisis y Diseño de Software
**Universidad Siglo 21 — Ingeniería en Sistemas de Información**  
Alumno: Fiume, Agustín | Legajo: VINF016173

## ¿De qué trata el proyecto?

Prototipo de un sistema de escritorio para gestionar turnos y consultas médicas en una clínica ambulatoria. Reemplaza el proceso manual actual (planillas de Excel por consultorio) con una aplicación centralizada desarrollada en Java y MySQL.

### Funcionalidades del prototipo

- Registro de pacientes con validación de DNI duplicado
- Asignación de turnos con control de solapamiento de horarios
- Cancelación de turnos
- Consulta del historial de turnos por paciente

---

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Interfaz gráfica | Java Swing |
| Base de datos | MySQL 8.0 |
| Conexión BD | JDBC + MySQL Connector/J 8.0 |
| IDE | IntelliJ IDEA |

---

## Estructura del proyecto

```
ap1_Fiume_Agustin/
├── schema.sql                          ← Script para crear la base de datos
├── README.md
└── src/
    └── main/
        └── java/
            └── com/clinica/
                ├── modelo/
                │   ├── Paciente.java
                │   └── Turno.java
                ├── dao/
                │   ├── Conexion.java
                │   ├── PacienteDAO.java
                │   └── TurnoDAO.java
                ├── servicio/
                │   ├── PacienteService.java
                │   └── TurnoService.java
                └── ui/
                    └── VentanaPrincipal.java
```

---

## Cómo ejecutarlo

### 1. Requisitos previos

- Java 17 o superior instalado
- MySQL 8.0 instalado y corriendo
- MySQL Connector/J 8.0 (el `.jar` se agrega al classpath del proyecto)

### 2. Crear la base de datos

Abrí MySQL Workbench o la terminal de MySQL y ejecutá el script:

```sql
source ruta/al/archivo/schema.sql
```

O copiá y pegá el contenido de `schema.sql` directamente en MySQL Workbench.

### 3. Configurar la conexión

En el archivo `src/main/java/com/clinica/dao/Conexion.java`, verificá que los datos de conexión sean correctos:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/clinica_turnos?serverTimezone=America/Argentina/Buenos_Aires";
private static final String USUARIO  = "root";      // ← tu usuario de MySQL
private static final String PASSWORD = "root";      // ← tu contraseña de MySQL
```

### 4. Agregar el conector de MySQL al proyecto

En IntelliJ IDEA:
1. `File` → `Project Structure` → `Libraries`
2. Clic en `+` → `Java`
3. Seleccioná el archivo `mysql-connector-j-8.x.x.jar`

### 5. Ejecutar

Corré la clase `VentanaPrincipal.java` como aplicación Java (método `main`).

---

## Arquitectura

El proyecto sigue una arquitectura en tres capas:

```
┌─────────────────────┐
│  Capa de Presentación│  VentanaPrincipal.java (Swing)
├─────────────────────┤
│  Lógica de Negocios  │  PacienteService.java / TurnoService.java
├─────────────────────┤
│  Acceso a Datos      │  PacienteDAO.java / TurnoDAO.java / Conexion.java
└─────────────────────┘
           │
      MySQL 8.0
```

---

## Datos de prueba

El script `schema.sql` ya incluye datos de prueba:
- 4 obras sociales
- 3 profesionales (Clínica Médica, Pediatría, Cardiología)
- 3 pacientes de ejemplo
- Horarios disponibles cargados

---

*Trabajo práctico desarrollado para la materia Análisis y Diseño de Software, Universidad Siglo 21, 2025.*
