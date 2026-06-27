package com.clinica.ui;
import com.clinica.archivo.LogOperaciones;
import com.clinica.dao.Conexion;
import com.clinica.excepciones.*;
import com.clinica.modelo.*;
import com.clinica.servicio.SistemaGestion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu principal del sistema. Interfaz de usuario por consola.
 * Conecta la capa de presentacion con la logica de negocios.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Menu {

    private final SistemaGestion sistema = new SistemaGestion();
    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void iniciar() {
        System.out.println("+-----------------------------------------+");
        System.out.println("|   Clinica Salud Integral S.R.L.         |");
        System.out.println("|   Sistema de Gestion de Turnos - v2.0   |");
        System.out.println("|   Fiume, Agustin Nicolas - VINF016173   |");
        System.out.println("+-----------------------------------------+");

        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();
            int op = leerEntero("Ingresa una opcion: ");
            switch (op) {
                case 1: menuPacientes();   break;
                case 2: menuTurnos();      break;
                case 3: menuMedicos();     break;
                case 4: menuReportes();    break;
                case 5: LogOperaciones.mostrar(); break;
                case 0:
                    System.out.println("\nCerrando conexion y saliendo...");
                    Conexion.cerrar();
                    continuar = false;
                    break;
                default: System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n+---------------------------------------+");
        System.out.println("|          MENU PRINCIPAL               |");
        System.out.println("+---------------------------------------+");
        System.out.println("|  1. Gestion de pacientes              |");
        System.out.println("|  2. Gestion de turnos                 |");
        System.out.println("|  3. Medicos de la clinica             |");
        System.out.println("|  4. Reportes y estadisticas           |");
        System.out.println("|  5. Ver log de operaciones            |");
        System.out.println("|  0. Salir                             |");
        System.out.println("+---------------------------------------+");
    }

    // ── Pacientes ──────────────────────────────────────────────
    private void menuPacientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n+---------------------------------------+");
            System.out.println("|       GESTION DE PACIENTES            |");
            System.out.println("+---------------------------------------+");
            System.out.println("|  1. Registrar nuevo paciente          |");
            System.out.println("|  2. Buscar por DNI                    |");
            System.out.println("|  3. Buscar por nombre                 |");
            System.out.println("|  4. Listar todos (orden alfabetico)   |");
            System.out.println("|  0. Volver                            |");
            System.out.println("+---------------------------------------+");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1: registrarPaciente();  break;
                case 2: buscarPorDni();       break;
                case 3: buscarPorNombre();    break;
                case 4: listarPacientes();    break;
                case 0: volver = true;        break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private void registrarPaciente() {
        System.out.println("\n--- Registrar nuevo paciente ---");
        try {
            String nombre   = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");
            String dni      = leerTexto("DNI: ");
            String telefono = leerTexto("Telefono (Enter para omitir): ");
            String email    = leerTexto("Email (Enter para omitir): ");
            LocalDate fechaNac = leerFecha("Fecha nac. (dd/MM/yyyy): ");

            // Mostrar especialidades usando el arreglo fijo
            System.out.println("Obra social: 1-OSDE  2-Swiss Medical  3-PAMI  4-Particular");
            int opOS = leerEntero("Opcion: ");
            String[] obras = {"OSDE","Swiss Medical","PAMI","Particular"};
            String os = (opOS >= 1 && opOS <= 4) ? obras[opOS-1] : "Particular";

            Paciente nuevo = new Paciente(0, nombre, apellido, dni,
                telefono.isEmpty() ? null : telefono,
                email.isEmpty()    ? null : email,
                fechaNac, os);
            sistema.registrarPaciente(nuevo);

        } catch (DniDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ConexionException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }

    private void buscarPorDni() {
        try {
            Paciente p = sistema.buscarPacientePorDni(leerTexto("DNI a buscar: "));
            System.out.println("\n--- Datos del paciente ---");
            p.mostrarInfo();
        } catch (PacienteNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ConexionException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }

    private void buscarPorNombre() {
        ArrayList<Paciente> res = sistema.buscarPacientesPorNombre(leerTexto("Nombre o apellido: "));
        if (res.isEmpty()) { System.out.println("No se encontraron pacientes."); return; }
        System.out.println("Se encontraron " + res.size() + " paciente(s):");
        for (Paciente p : res) System.out.println("  * " + p);
    }

    private void listarPacientes() {
        ArrayList<Paciente> lista = sistema.listarPacientesOrdenados();
        if (lista.isEmpty()) { System.out.println("No hay pacientes registrados."); return; }
        System.out.println("\n=== Pacientes (orden alfabetico) ===");
        int i = 1;
        for (Paciente p : lista)
            System.out.println(i++ + ". " + p.getNombreCompleto()
                + " | DNI: " + p.getDni() + " | " + p.calcularEdad() + " anios");
    }

    // ── Turnos ─────────────────────────────────────────────────
    private void menuTurnos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n+---------------------------------------+");
            System.out.println("|         GESTION DE TURNOS             |");
            System.out.println("+---------------------------------------+");
            System.out.println("|  1. Asignar nuevo turno               |");
            System.out.println("|  2. Cancelar turno                    |");
            System.out.println("|  3. Registrar atencion                |");
            System.out.println("|  4. Ver historial de un paciente      |");
            System.out.println("|  5. Listar todos los turnos           |");
            System.out.println("|  0. Volver                            |");
            System.out.println("+---------------------------------------+");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1: asignarTurno();      break;
                case 2: cancelarTurno();     break;
                case 3: registrarAtencion(); break;
                case 4: verHistorial();      break;
                case 5: listarTurnos();      break;
                case 0: volver = true;       break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private void asignarTurno() {
        System.out.println("\n--- Asignar nuevo turno ---");
        try {
            Paciente paciente = sistema.buscarPacientePorDni(leerTexto("DNI del paciente: "));
            System.out.println("Paciente: " + paciente.getNombreCompleto());

            ArrayList<Medico> medicos = sistema.listarMedicos();
            System.out.println("\nMedicos disponibles:");
            for (int i = 0; i < medicos.size(); i++)
                System.out.println("  " + (i+1) + ". " + medicos.get(i).getNombreCompleto()
                    + " (" + medicos.get(i).getEspecialidad() + ")");

            int opMed = leerEntero("Selecciona un medico: ");
            if (opMed < 1 || opMed > medicos.size()) { System.out.println("Opcion invalida."); return; }
            Medico medico = medicos.get(opMed - 1);

            LocalDateTime fechaHora = leerFechaHora("Fecha y hora (dd/MM/yyyy HH:mm): ");
            String motivo = leerTexto("Motivo de la consulta: ");

            sistema.asignarTurno(paciente.getId(), medico.getId(), fechaHora, motivo);

        } catch (PacienteNoEncontradoException | HorarioOcupadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ConexionException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }

    private void cancelarTurno() {
        listarTurnos();
        int id = leerEntero("Numero de turno a cancelar: ");
        System.out.print("Confirmas la cancelacion? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            try { sistema.cancelarTurno(id); }
            catch (ConexionException e) { System.out.println("Error: " + e.getMessage()); }
        } else {
            System.out.println("Cancelacion abortada.");
        }
    }

    private void registrarAtencion() {
        System.out.println("\n--- Registrar atencion ---");
        int id      = leerEntero("Numero de turno: ");
        String diag = leerTexto("Diagnostico: ");
        String trat = leerTexto("Tratamiento indicado: ");
        try { sistema.atenderTurno(id, diag, trat); }
        catch (ConexionException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void verHistorial() {
        try {
            Paciente p = sistema.buscarPacientePorDni(leerTexto("DNI del paciente: "));
            ArrayList<Turno> hist = sistema.listarHistorialPaciente(p.getId());
            System.out.println("\nHistorial de " + p.getNombreCompleto() + ":");
            if (hist.isEmpty()) { System.out.println("  No tiene turnos registrados."); return; }
            for (Turno t : hist) { System.out.println("----------------------------------------"); t.mostrarInfo(); }
        } catch (PacienteNoEncontradoException | ConexionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarTurnos() {
        try {
            ArrayList<Turno> todos = sistema.listarTodosLosTurnos();
            if (todos.isEmpty()) { System.out.println("No hay turnos registrados."); return; }
            System.out.println("\n=== Todos los turnos ===");
            for (Turno t : todos) System.out.println("  " + t);
        } catch (ConexionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── Medicos ────────────────────────────────────────────────
    private void menuMedicos() {
        try {
            ArrayList<Medico> medicos = sistema.listarMedicos();
            System.out.println("\n=== Medicos de la clinica ===");
            for (Medico m : medicos) {
                System.out.println("----------------------------------------");
                m.mostrarInfo();
            }
            // Mostrar especialidades disponibles usando arreglo fijo
            System.out.println("\nEspecialidades disponibles en la clinica:");
            String[] esp = sistema.getEspecialidades();
            for (int i = 0; i < esp.length; i++)
                System.out.println("  " + (i+1) + ". " + esp[i]);
        } catch (ConexionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── Reportes ────────────────────────────────────────────────
    private void menuReportes() {
        System.out.println("\n=== REPORTES Y ESTADISTICAS ===");
        try {
            // Usar el arreglo de estadisticas
            int[] stats = sistema.obtenerEstadisticas();
            int total = stats[0] + stats[1] + stats[2];
            System.out.println("Total de turnos     : " + total);
            System.out.println("  -> Activos        : " + stats[0]);
            System.out.println("  -> Atendidos      : " + stats[1]);
            System.out.println("  -> Cancelados     : " + stats[2]);
            System.out.println("Total de pacientes  : " + sistema.listarPacientesOrdenados().size());
            System.out.println("Total de medicos    : " + sistema.listarMedicos().size());
        } catch (ConexionException e) {
            System.out.println("Error al obtener reportes: " + e.getMessage());
        }
    }

    // ── Helpers ────────────────────────────────────────────────
    private int leerEntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero entero valido.");
            }
        }
    }

    private String leerTexto(String msg) {
        System.out.print(msg); return scanner.nextLine().trim();
    }

    private LocalDate leerFecha(String msg) {
        while (true) {
            try { return LocalDate.parse(leerTexto(msg), FMT_D); }
            catch (DateTimeParseException e) { System.out.println("Formato incorrecto. Usa dd/MM/yyyy"); }
        }
    }

    private LocalDateTime leerFechaHora(String msg) {
        while (true) {
            try { return LocalDateTime.parse(leerTexto(msg), FMT_DT); }
            catch (DateTimeParseException e) { System.out.println("Formato incorrecto. Usa dd/MM/yyyy HH:mm"); }
        }
    }

    public static void main(String[] args) { new Menu().iniciar(); }
}
