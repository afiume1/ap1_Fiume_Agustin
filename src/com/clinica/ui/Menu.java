package com.clinica.ui;
import com.clinica.excepciones.*;
import com.clinica.modelo.*;
import com.clinica.servicio.SistemaGestion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu principal del sistema por consola.
 * Usa switch (condicional) y while (repetitiva) para controlar el flujo.
 * Maneja todas las excepciones con try-catch.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Menu {
    private final SistemaGestion sistema = new SistemaGestion();
    private final Scanner        scanner = new Scanner(System.in);
    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void iniciar() {
        System.out.println("+-----------------------------------------+");
        System.out.println("|  Clinica Salud Integral S.R.L.          |");
        System.out.println("|  Sistema de Gestion de Turnos            |");
        System.out.println("|  Fiume, Agustin Nicolas - VINF016173    |");
        System.out.println("+-----------------------------------------+");

        boolean continuar = true;
        // Estructura repetitiva: while mantiene el menu activo
        while (continuar) {
            mostrarMenuPrincipal();
            int op = leerEntero("Ingresa una opcion: ");
            // Estructura condicional: switch para manejar opciones
            switch (op) {
                case 1: menuPacientes();  break;
                case 2: menuTurnos();     break;
                case 3: menuPersonal();   break;
                case 4: menuReportes();   break;
                case 0: System.out.println("\nSaliendo del sistema. Hasta luego!"); continuar = false; break;
                default: System.out.println("Opcion invalida. Ingresa un numero del 0 al 4.");
            }
        }
        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n+-------------------------------------+");
        System.out.println("|         MENU PRINCIPAL              |");
        System.out.println("+-------------------------------------+");
        System.out.println("|  1. Gestion de pacientes            |");
        System.out.println("|  2. Gestion de turnos               |");
        System.out.println("|  3. Personal de la clinica          |");
        System.out.println("|  4. Reportes                        |");
        System.out.println("|  0. Salir                           |");
        System.out.println("+-------------------------------------+");
    }

    // -- Submenu Pacientes --
    private void menuPacientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n+-------------------------------------+");
            System.out.println("|      GESTION DE PACIENTES           |");
            System.out.println("+-------------------------------------+");
            System.out.println("|  1. Registrar nuevo paciente        |");
            System.out.println("|  2. Buscar por DNI                  |");
            System.out.println("|  3. Buscar por nombre               |");
            System.out.println("|  4. Listar todos (orden alfabetico) |");
            System.out.println("|  0. Volver                          |");
            System.out.println("+-------------------------------------+");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1: registrarPaciente();   break;
                case 2: buscarPorDni();        break;
                case 3: buscarPorNombre();     break;
                case 4: listarPacientes();     break;
                case 0: volver = true;         break;
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
            LocalDate fechaNac = null;
            boolean ok = false;
            while (!ok) {
                try {
                    fechaNac = LocalDate.parse(leerTexto("Fecha nac. (dd/MM/yyyy): "), FMT_D);
                    ok = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato incorrecto. Usa dd/MM/yyyy");
                }
            }
            System.out.println("Obra social: 1-OSDE  2-Swiss Medical  3-PAMI  4-Particular");
            int opOS = leerEntero("Opcion: ");
            String[] obras = {"OSDE","Swiss Medical","PAMI","Particular"};
            String os = (opOS >= 1 && opOS <= 4) ? obras[opOS-1] : "Particular";
            Paciente nuevo = new Paciente(0, nombre, apellido, dni,
                telefono.trim().isEmpty() ? null : telefono,
                email.trim().isEmpty()    ? null : email,
                fechaNac, os);
            sistema.registrarPaciente(nuevo);
        } catch (DniDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private void buscarPorDni() {
        try {
            Paciente p = sistema.buscarPacientePorDni(leerTexto("DNI a buscar: "));
            System.out.println("\n--- Datos del paciente ---");
            p.mostrarInfo();
        } catch (PacienteNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarPorNombre() {
        List<Paciente> res = sistema.buscarPacientesPorNombre(leerTexto("Nombre o apellido: "));
        if (res.isEmpty()) { System.out.println("No se encontraron pacientes."); return; }
        System.out.println("Se encontraron " + res.size() + " paciente(s):");
        for (Paciente p : res) System.out.println("  * " + p);
    }

    private void listarPacientes() {
        List<Paciente> lista = sistema.listarPacientesOrdenados();
        if (lista.isEmpty()) { System.out.println("No hay pacientes registrados."); return; }
        System.out.println("\n=== Pacientes (orden alfabetico) ===");
        int i = 1;
        for (Paciente p : lista)
            System.out.println(i++ + ". " + p.getNombreCompleto() + " | DNI: " + p.getDni()
                             + " | " + p.getObraSocial() + " | " + p.calcularEdad() + " anios");
    }

    // -- Submenu Turnos --
    private void menuTurnos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n+-------------------------------------+");
            System.out.println("|        GESTION DE TURNOS            |");
            System.out.println("+-------------------------------------+");
            System.out.println("|  1. Asignar nuevo turno             |");
            System.out.println("|  2. Cancelar turno                  |");
            System.out.println("|  3. Registrar atencion              |");
            System.out.println("|  4. Ver historial de paciente       |");
            System.out.println("|  5. Listar todos los turnos         |");
            System.out.println("|  0. Volver                          |");
            System.out.println("+-------------------------------------+");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1: asignarTurno();       break;
                case 2: cancelarTurno();      break;
                case 3: registrarAtencion();  break;
                case 4: verHistorial();       break;
                case 5: listarTurnos();       break;
                case 0: volver = true;        break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private void asignarTurno() {
        System.out.println("\n--- Asignar nuevo turno ---");
        try {
            Paciente paciente = sistema.buscarPacientePorDni(leerTexto("DNI del paciente: "));
            System.out.println("Paciente: " + paciente.getNombreCompleto());
            List<Medico> medicos = sistema.listarMedicos();
            System.out.println("\nMedicos disponibles:");
            for (int i = 0; i < medicos.size(); i++)
                System.out.println("  " + (i+1) + ". " + medicos.get(i).getNombreCompleto()
                    + " (" + medicos.get(i).getEspecialidad() + ") - " + medicos.get(i).getHorarioAtencion());
            int opMed = leerEntero("Selecciona un medico: ");
            if (opMed < 1 || opMed > medicos.size()) { System.out.println("Opcion invalida."); return; }
            Medico medico = medicos.get(opMed - 1);
            LocalDateTime fechaHora = null;
            boolean ok = false;
            while (!ok) {
                try {
                    fechaHora = LocalDateTime.parse(leerTexto("Fecha y hora (dd/MM/yyyy HH:mm): "), FMT_DT);
                    ok = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato incorrecto. Usa dd/MM/yyyy HH:mm");
                }
            }
            String motivo = leerTexto("Motivo de la consulta: ");
            sistema.asignarTurno(paciente, medico, fechaHora, motivo);
        } catch (PacienteNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (HorarioOcupadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private void cancelarTurno() {
        listarTurnos();
        int id = leerEntero("Numero de turno a cancelar: ");
        System.out.print("Confirma la cancelacion del turno #" + id + "? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) sistema.cancelarTurno(id);
        else System.out.println("Cancelacion abortada.");
    }

    private void registrarAtencion() {
        System.out.println("\n--- Registrar atencion ---");
        int id       = leerEntero("Numero de turno: ");
        String diag  = leerTexto("Diagnostico: ");
        String trat  = leerTexto("Tratamiento indicado: ");
        sistema.atenderTurno(id, diag, trat);
    }

    private void verHistorial() {
        try {
            Paciente p = sistema.buscarPacientePorDni(leerTexto("DNI del paciente: "));
            List<Turno> hist = sistema.listarHistorialPaciente(p);
            System.out.println("\nHistorial de " + p.getNombreCompleto() + ":");
            if (hist.isEmpty()) { System.out.println("  No tiene turnos registrados."); return; }
            for (Turno t : hist) { System.out.println("-".repeat(50)); t.mostrarInfo(); }
        } catch (PacienteNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarTurnos() {
        List<Turno> todos = sistema.getTurnos();
        if (todos.isEmpty()) { System.out.println("No hay turnos registrados."); return; }
        System.out.println("\n=== Todos los turnos ===");
        for (Turno t : todos) System.out.println("  " + t);
    }

    // -- Submenu Personal --
    private void menuPersonal() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n+-------------------------------------+");
            System.out.println("|      PERSONAL DE LA CLINICA         |");
            System.out.println("+-------------------------------------+");
            System.out.println("|  1. Ver todo el personal            |");
            System.out.println("|  2. Listar solo medicos             |");
            System.out.println("|  3. Turnos activos de un medico     |");
            System.out.println("|  0. Volver                          |");
            System.out.println("+-------------------------------------+");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1: sistema.mostrarTodoElPersonal(); break;
                case 2: listarMedicos();                 break;
                case 3: turnosDeUnMedico();              break;
                case 0: volver = true;                   break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private void listarMedicos() {
        System.out.println("\n=== Medicos de la clinica ===");
        for (Medico m : sistema.listarMedicos()) { System.out.println("-".repeat(40)); m.mostrarInfo(); }
    }

    private void turnosDeUnMedico() {
        List<Medico> medicos = sistema.listarMedicos();
        for (int i = 0; i < medicos.size(); i++)
            System.out.println("  " + (i+1) + ". " + medicos.get(i).getNombreCompleto());
        int op = leerEntero("Selecciona un medico: ");
        if (op < 1 || op > medicos.size()) { System.out.println("Opcion invalida."); return; }
        Medico m = medicos.get(op - 1);
        List<Turno> activos = sistema.listarTurnosActivosDeMedico(m);
        System.out.println("\nTurnos activos de " + m.getNombreCompleto() + ":");
        if (activos.isEmpty()) System.out.println("  No tiene turnos activos.");
        else for (Turno t : activos) System.out.println("  " + t);
    }

    // -- Reportes --
    private void menuReportes() {
        System.out.println("\n=== REPORTES ===");
        System.out.println("Total de pacientes  : " + sistema.getPacientes().size());
        System.out.println("Total de turnos     : " + sistema.getTurnos().size());
        long activos=0, atendidos=0, cancelados=0;
        for (Turno t : sistema.getTurnos()) {
            if      (t.getEstado() == Turno.Estado.ACTIVO)    activos++;
            else if (t.getEstado() == Turno.Estado.ATENDIDO)  atendidos++;
            else                                               cancelados++;
        }
        System.out.println("  -> Activos         : " + activos);
        System.out.println("  -> Atendidos       : " + atendidos);
        System.out.println("  -> Cancelados      : " + cancelados);
        System.out.println("Total de medicos    : " + sistema.listarMedicos().size());
    }

    // -- Helpers --
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
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    public static void main(String[] args) { new Menu().iniciar(); }
}
