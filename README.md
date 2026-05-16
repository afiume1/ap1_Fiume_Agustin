# Sistema de Gestión de Turnos y Consultas
### Actividades Prácticas N.° 1 y 2 — Análisis y Diseño de Software
**Universidad Siglo 21 — Licenciatura en Informática**  
Alumno: Fiume, Agustín Nicolás | Legajo: VINF016173

---

## Descripción

Prototipo de un sistema de escritorio para gestionar turnos y consultas médicas en una clínica ambulatoria (Clínica Salud Integral S.R.L.). Reemplaza el proceso manual actual —planillas de Excel por consultorio— con una aplicación centralizada desarrollada en Java y MySQL.

### Casos de uso implementados en el prototipo

| CU | Nombre | Estado |
|---|---|---|
| CU-01 | Registrar paciente | ✅ Implementado |
| CU-02 | Asignar turno | ✅ Implementado |
| CU-03 | Cancelar turno | ✅ Implementado |
| CU-04 | Consultar historial del paciente | ✅ Implementado |
| CU-05 | Registrar consulta | 🔜 Próxima iteración |
| CU-06 | Gestionar profesionales | 🔜 Próxima iteración |
| CU-07 | Generar reporte | 🔜 Próxima iteración |
| CU-08 | Autenticar usuario | 🔜 Próxima iteración |
| CU-09 | Verificar disponibilidad (incluido en CU-02) | ✅ Implementado |

---

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 17 (LTS) |
| Interfaz gráfica | Java Swing |
| Base de datos | MySQL 8.0 |
| Conexión BD | JDBC + MySQL Connector/J 8 |
| IDE recomendado | IntelliJ IDEA |
| Control de versiones | Git + GitHub |

---

## Estructura del proyecto

```
ap1_Fiume_Agustin/
├── schema.sql                                  ← Base de datos + datos de prueba + consultas SQL
├── README.md
└── src/
    └── main/
        └── java/
            └── com/clinica/
                ├── modelo/
                │   ├── Paciente.java
                │   └── Turno.java
                ├── repositorio/                ← Capa de acceso a datos (JDBC)
                │   ├── Conexion.java
                │   ├── PacienteRepositorio.java
                │   └── TurnoRepositorio.java
                ├── servicio/                   ← Capa de lógica de negocios
                │   ├── HashUtil.java
                │   ├── PacienteService.java
                │   └── TurnoService.java
                └── ui/                         ← Capa de presentación (Swing)
                    └── VentanaPrincipal.java
```

---

## Arquitectura

El sistema sigue una arquitectura en tres capas (ver TP2, sección 12.1):

```
┌──────────────────────────────────┐
│  Presentación  (Java Swing)      │  VentanaPrincipal.java
├──────────────────────────────────┤
│  Lógica de negocios (Servicio)   │  PacienteService / TurnoService / HashUtil
├──────────────────────────────────┤
│  Acceso a datos (Repositorio)    │  PacienteRepositorio / TurnoRepositorio / Conexion
└──────────────────────────────────┘
                │  JDBC (TCP/IP puerto 3306)
          MySQL 8.0 Server
```

---

## Cómo ejecutarlo

### 1. Requisitos previos

- Java 17 o superior instalado (`java -version` para verificar)
- MySQL 8.0 instalado y corriendo
- MySQL Connector/J 8 (el archivo `.jar` se agrega al classpath del proyecto)

### 2. Crear la base de datos

Abrí MySQL Workbench o la terminal de MySQL y ejecutá:

```bash
source /ruta/al/archivo/schema.sql
```

O copiá y pegá el contenido de `schema.sql` directamente en MySQL Workbench.

El script crea la base de datos, todas las tablas y carga datos de prueba automáticamente.

### 3. Configurar la conexión

En `src/main/java/com/clinica/repositorio/Conexion.java`, ajustá los datos de conexión si es necesario:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/clinica_turnos"
                                     + "?serverTimezone=America/Argentina/Buenos_Aires";
private static final String USUARIO  = "root";      // ← tu usuario de MySQL
private static final String PASSWORD = "root";      // ← tu contraseña de MySQL
```

### 4. Agregar el conector de MySQL al proyecto

En **IntelliJ IDEA**:
1. `File` → `Project Structure` → `Libraries`
2. Clic en `+` → `Java`
3. Seleccioná el archivo `mysql-connector-j-8.x.x.jar`
4. Aplicar y cerrar

### 5. Ejecutar

Corré la clase `VentanaPrincipal.java` como aplicación Java (botón ▶ en IntelliJ o método `main`).

---

## Seguridad

Las contraseñas se almacenan usando hash SHA-256 (ver `HashUtil.java`), cumpliendo con el RNF03 definido en el TP2. Nunca se almacena la contraseña en texto plano.

---

## Datos de prueba incluidos

El `schema.sql` incluye:
- 4 obras sociales
- 4 especialidades médicas
- 3 profesionales con horarios cargados
- 3 pacientes de ejemplo
- 2 turnos activos y 1 atendido con consulta registrada

---

*Trabajo práctico desarrollado para la materia Análisis y Diseño de Software, Universidad Siglo 21, 2025.*
