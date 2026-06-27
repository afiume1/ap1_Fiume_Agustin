# Sistema de Gestion de Turnos - v2.0
### Actividad Practica N.4 - Proyecto Integrador
**Universidad Siglo 21 | Fiume, Agustin Nicolas | VINF016173**

## Patrones de diseno aplicados
- **Singleton**: clase Conexion - unica instancia de conexion MySQL
- **DAO/Repositorio**: PacienteDAO, MedicoDAO, TurnoDAO implementan IRepositorio<T>

## Estructura
```
src/com/clinica/
├── modelo/      Persona(abs), Paciente, Profesional(abs), Medico, Turno
├── dao/         IRepositorio(interfaz), Conexion(Singleton), PacienteDAO, MedicoDAO, TurnoDAO
├── servicio/    SistemaGestion
├── ui/          Menu  <- main() esta aqui
├── excepciones/ DniDuplicadoException, HorarioOcupadoException,
│                PacienteNoEncontradoException, ConexionException
├── util/        Ordenamiento (Bubble Sort + busqueda lineal)
└── archivo/     LogOperaciones (lectura/escritura de archivos)
```

## Configuracion
1. Ejecutar schema.sql en MySQL 8.0
2. Descargar mysql-connector-j-8.x.x.jar de https://dev.mysql.com/downloads/connector/j/
3. Ajustar usuario/password en: src/com/clinica/dao/Conexion.java

## Compilar y ejecutar (Windows PowerShell)
```powershell
mkdir out
javac -encoding UTF-8 -cp mysql-connector-j-8.x.x.jar -d out src\com\clinica\modelo\Persona.java src\com\clinica\modelo\Paciente.java src\com\clinica\modelo\Profesional.java src\com\clinica\modelo\Medico.java src\com\clinica\modelo\Turno.java src\com\clinica\dao\IRepositorio.java src\com\clinica\dao\Conexion.java src\com\clinica\dao\PacienteDAO.java src\com\clinica\dao\MedicoDAO.java src\com\clinica\dao\TurnoDAO.java src\com\clinica\excepciones\DniDuplicadoException.java src\com\clinica\excepciones\HorarioOcupadoException.java src\com\clinica\excepciones\PacienteNoEncontradoException.java src\com\clinica\excepciones\ConexionException.java src\com\clinica\archivo\LogOperaciones.java src\com\clinica\util\Ordenamiento.java src\com\clinica\servicio\SistemaGestion.java src\com\clinica\ui\Menu.java
java -cp "out;mysql-connector-j-8.x.x.jar" com.clinica.ui.Menu
```
