package com.clinica.servicio;
import com.clinica.excepciones.*;
import com.clinica.modelo.*;
import com.clinica.util.Ordenamiento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa de logica de negocios. Gestiona listas de objetos y coordina operaciones.
 * Demuestra POLIMORFISMO al tratar Medico y Administrativo como Profesional.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class SistemaGestion {
    private List<Paciente>    pacientes  = new ArrayList<>();
    private List<Profesional> personal   = new ArrayList<>();
    private List<Turno>       turnos     = new ArrayList<>();
    private int               proximoIdPaciente = 1;
    private int               proximoIdTurno    = 1;

    public SistemaGestion() { cargarDatosDePrueba(); }

    // -- Pacientes --
    public void registrarPaciente(Paciente paciente) {
        for (Paciente p : pacientes) {
            if (p.getDni().equals(paciente.getDni()))
                throw new DniDuplicadoException(paciente.getDni());
        }
        paciente.setId(proximoIdPaciente++);
        pacientes.add(paciente);
        System.out.println("Paciente registrado: " + paciente.getNombreCompleto());
    }

    public Paciente buscarPacientePorDni(String dni) {
        for (Paciente p : pacientes) {
            if (p.getDni().equals(dni)) return p;
        }
        throw new PacienteNoEncontradoException("DNI: " + dni);
    }

    public List<Paciente> listarPacientesOrdenados() {
        List<Paciente> copia = new ArrayList<>(pacientes);
        Ordenamiento.ordenarPacientesPorApellido(copia);
        return copia;
    }

    public List<Paciente> buscarPacientesPorNombre(String texto) {
        return Ordenamiento.buscarPacientesPorNombre(pacientes, texto);
    }

    // -- Turnos --
    public void asignarTurno(Paciente paciente, Medico medico,
                             LocalDateTime fechaHora, String motivo) {
        for (Turno t : turnos) {
            if (t.getMedico().getId() == medico.getId()
                    && t.getFechaHora().equals(fechaHora)
                    && t.getEstado() == Turno.Estado.ACTIVO)
                throw new HorarioOcupadoException(medico.getNombreCompleto(), fechaHora.toString());
        }
        Turno turno = new Turno(proximoIdTurno++, paciente, medico, fechaHora, motivo);
        turnos.add(turno);
        System.out.println("Turno #" + turno.getIdTurno() + " asignado correctamente.");
    }

    public void cancelarTurno(int idTurno) {
        Ordenamiento.ordenarTurnosPorId(turnos);
        Turno turno = Ordenamiento.busquedaBinariaTurno(turnos, idTurno);
        if (turno == null) { System.out.println("No se encontro el turno #" + idTurno); return; }
        if (turno.getEstado() != Turno.Estado.ACTIVO) {
            System.out.println("El turno #" + idTurno + " no esta activo [" + turno.getEstado() + "]"); return;
        }
        turno.cancelar();
        System.out.println("Turno #" + idTurno + " cancelado.");
    }

    public void atenderTurno(int idTurno, String diagnostico, String tratamiento) {
        Ordenamiento.ordenarTurnosPorId(turnos);
        Turno turno = Ordenamiento.busquedaBinariaTurno(turnos, idTurno);
        if (turno == null) { System.out.println("No se encontro el turno #" + idTurno); return; }
        if (turno.getEstado() != Turno.Estado.ACTIVO) {
            System.out.println("El turno #" + idTurno + " no esta activo."); return;
        }
        turno.atender(diagnostico, tratamiento);
        System.out.println("Turno #" + idTurno + " registrado como atendido.");
    }

    public List<Turno> listarTurnosActivosDeMedico(Medico medico) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos)
            if (t.getMedico().getId() == medico.getId() && t.getEstado() == Turno.Estado.ACTIVO)
                resultado.add(t);
        return resultado;
    }

    public List<Turno> listarHistorialPaciente(Paciente paciente) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos)
            if (t.getPaciente().getId() == paciente.getId()) resultado.add(t);
        return resultado;
    }

    public List<Medico> listarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        for (Profesional prof : personal)
            if (prof instanceof Medico) medicos.add((Medico) prof);
        return medicos;
    }

    /** Demuestra POLIMORFISMO: cada objeto ejecuta su propia version de mostrarInfo(). */
    public void mostrarTodoElPersonal() {
        System.out.println("\n=== Personal de la clinica ===");
        for (Profesional prof : personal) {
            System.out.println("-".repeat(40));
            prof.mostrarInfo();
        }
    }

    public List<Paciente>    getPacientes() { return pacientes; }
    public List<Profesional> getPersonal()  { return personal; }
    public List<Turno>       getTurnos()    { return turnos; }

    private void cargarDatosDePrueba() {
        Medico m1 = new Medico(1,"Maria","Gonzalez","20111000","1143001100","mg@clinica.com","MP-12345","Clinica Medica","Lunes y Miercoles 08:00-12:00");
        Medico m2 = new Medico(2,"Carlos","Rodriguez","22222000","1155002200","cr@clinica.com","MP-67890","Pediatria","Martes y Jueves 09:00-13:00");
        Medico m3 = new Medico(3,"Laura","Martinez","24333000","1167003300","lm@clinica.com","MP-11111","Cardiologia","Viernes 14:00-18:00");
        Administrativo a1 = new Administrativo(4,"Roberto","Sanchez","26444000","1179004400","rs@clinica.com","ADM-001","Recepcion");
        personal.add(m1); personal.add(m2); personal.add(m3); personal.add(a1);

        Paciente p1 = new Paciente(proximoIdPaciente++,"Juan","Perez","30111222","1143001122","juan@mail.com",LocalDate.of(1985,3,15),"OSDE");
        Paciente p2 = new Paciente(proximoIdPaciente++,"Ana","Lopez","27333444","1155002233","ana@mail.com",LocalDate.of(1990,7,22),"Swiss Medical");
        Paciente p3 = new Paciente(proximoIdPaciente++,"Carlos","Fernandez","29555666","1167003344","carlos@mail.com",LocalDate.of(1978,11,5),"PAMI");
        pacientes.add(p1); pacientes.add(p2); pacientes.add(p3);

        LocalDateTime ahora = LocalDateTime.now();
        Turno t1 = new Turno(proximoIdTurno++, p1, m1, ahora.plusDays(1).withHour(9).withMinute(0), "Control anual");
        Turno t2 = new Turno(proximoIdTurno++, p2, m2, ahora.plusDays(2).withHour(10).withMinute(0), "Fiebre persistente");
        Turno t3 = new Turno(proximoIdTurno++, p3, m3, ahora.minusDays(1).withHour(14).withMinute(0), "Dolor de pecho");
        t3.atender("Hipertension arterial leve","Enalapril 5mg cada 12hs");
        turnos.add(t1); turnos.add(t2); turnos.add(t3);
    }
}
