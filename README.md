# Sistema de Gestion de Turnos y Consultas
### Actividades Practicas N.1, 2 y 3 - Analisis y Diseno de Software
**Universidad Siglo 21 - Licenciatura en Informatica**
Alumno: Fiume, Agustin Nicolas | Legajo: VINF016173

## Descripcion
Sistema de gestion de turnos y consultas medicas para Clinica Salud Integral S.R.L.
Desarrollado en Java 17 con interfaz por consola, aplicando los 4 pilares de POO.

## Estructura
```
src/com/clinica/
├── modelo/         Persona(abs), Paciente, Profesional(abs), Medico, Administrativo, Turno
├── servicio/       SistemaGestion
├── ui/             Menu (punto de entrada: main en Menu.java)
├── excepciones/    DniDuplicadoException, HorarioOcupadoException, PacienteNoEncontradoException
└── util/           Ordenamiento (Bubble Sort + busqueda binaria)
```

## Compilar y ejecutar
```bash
# Compilar
javac -encoding UTF-8 -d out $(find src -name "*.java")

# Ejecutar
java -cp out com.clinica.ui.Menu
```

## Base de datos
Ejecutar schema.sql en MySQL 8.0 para crear la base de datos con datos de prueba.
Ajustar usuario/contrasena en: src/com/clinica/repositorio/Conexion.java
