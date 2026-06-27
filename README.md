# Sistema de Gestion de Turnos - v2.0
### Actividad Practica N.4 - Proyecto Integrador
**Universidad Siglo 21 | Fiume, Agustin Nicolas | VINF016173**

## Descripcion
Sistema de gestion de turnos y consultas medicas para Clinica Salud Integral S.R.L.
Desarrollado en Java 21 con interfaz por consola, patron Singleton + DAO y conexion real a MySQL.

## Estructura del proyecto
```
ap1_Fiume_Agustin/
├── src/com/clinica/
│   ├── modelo/        Persona, Paciente, Profesional, Medico, Turno
│   ├── dao/           IRepositorio, Conexion (Singleton), PacienteDAO, MedicoDAO, TurnoDAO
│   ├── servicio/      SistemaGestion
│   ├── ui/            Menu  <- punto de entrada (main aqui)
│   ├── excepciones/   DniDuplicadoException, HorarioOcupadoException,
│   │                  PacienteNoEncontradoException, ConexionException
│   ├── util/          Ordenamiento (Bubble Sort + busqueda lineal)
│   └── archivo/       LogOperaciones (lectura/escritura de archivos)
├── schema.sql          <- script para crear la base de datos
├── README.md
└── mysql-connector-j-9.x.x.jar  <- agregar manualmente (ver paso 2)
```

---

## Requisitos previos
- Java 17 o superior instalado (`java -version` para verificar)
- MySQL 8.0 instalado y corriendo
- MySQL Connector/J 9.x descargado (ver paso 2)

---

## Paso 1: Crear la base de datos en MySQL

1. Abrir **MySQL Workbench** y conectarse al servidor local
2. En el menu superior ir a **File -> Open SQL Script**
3. Buscar y abrir el archivo **schema.sql** que esta en la carpeta raiz del proyecto
4. Presionar el boton del rayo **(Execute)** o **Ctrl+Shift+Enter**
5. Verificar que se creo correctamente ejecutando en Workbench:
   ```sql
   SHOW DATABASES;
   ```
   Debe aparecer `clinica_turnos` en la lista.

El script crea automaticamente la base de datos, todas las tablas y carga datos de prueba.

---

## Paso 2: Descargar el conector de MySQL

1. Entrar a https://dev.mysql.com/downloads/connector/j/
2. En **Select Operating System** elegir **Platform Independent**
3. Descargar el **ZIP Archive**
4. Descomprimir y copiar el archivo `mysql-connector-j-9.x.x.jar` a la carpeta raiz del proyecto (al lado de `src/`)

---

## Paso 3: Configurar la conexion

Abrir el archivo `src/com/clinica/dao/Conexion.java` y ajustar:

```java
private static final String USUARIO  = "root";       // tu usuario de MySQL
private static final String PASSWORD = "tu_clave";   // la misma que usas en Workbench
```

---

## Paso 4: Compilar (PowerShell desde la carpeta del proyecto)

```powershell
mkdir out

javac -encoding UTF-8 -cp mysql-connector-j-9.x.x.jar -d out src\com\clinica\modelo\Persona.java src\com\clinica\modelo\Paciente.java src\com\clinica\modelo\Profesional.java src\com\clinica\modelo\Medico.java src\com\clinica\modelo\Turno.java src\com\clinica\dao\IRepositorio.java src\com\clinica\dao\Conexion.java src\com\clinica\dao\PacienteDAO.java src\com\clinica\dao\MedicoDAO.java src\com\clinica\dao\TurnoDAO.java src\com\clinica\excepciones\DniDuplicadoException.java src\com\clinica\excepciones\HorarioOcupadoException.java src\com\clinica\excepciones\PacienteNoEncontradoException.java src\com\clinica\excepciones\ConexionException.java src\com\clinica\archivo\LogOperaciones.java src\com\clinica\util\Ordenamiento.java src\com\clinica\servicio\SistemaGestion.java src\com\clinica\ui\Menu.java
```

> Reemplazar `9.x.x` por el numero de version exacto del JAR descargado.
> Cada vez que se modifica `Conexion.java` hay que volver a compilar.

---

## Paso 5: Ejecutar

```powershell
java -cp "out;mysql-connector-j-9.x.x.jar" com.clinica.ui.Menu
```

---

## Errores comunes

| Error | Causa | Solucion |
|---|---|---|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | El JAR no esta en el classpath | Verificar que el .jar este en la carpeta y usar `-cp jar` al compilar y ejecutar |
| `Access denied for user 'root'` | Contrasena incorrecta | Corregir PASSWORD en Conexion.java y recompilar |
| `Unknown database 'clinica_turnos'` | La BD no fue creada | Ejecutar schema.sql en MySQL Workbench |
| `Communications link failure` | MySQL no esta corriendo | Iniciar el servicio MySQL desde MySQL Workbench |

---

## Patrones de diseno aplicados
- **Singleton**: `Conexion.java` — unica instancia de conexion MySQL en todo el sistema
- **DAO + Interfaz**: `IRepositorio<T>` implementada por `PacienteDAO`, `MedicoDAO` y `TurnoDAO`

---
