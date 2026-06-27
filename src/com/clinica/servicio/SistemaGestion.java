package com.clinica.servicio;
import com.clinica.archivo.LogOperaciones;
import com.clinica.dao.*;
import com.clinica.excepciones.*;
import com.clinica.modelo.*;
import com.clinica.util.Ordenamiento;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Capa de logica de negocios. Coordina DAOs y aplica reglas del negocio.
 * Usa arreglos para estadisticas y ArrayList para listas dinamicas.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class SistemaGestion {

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO   medicoDAO   = new MedicoDAO();
    private final TurnoDAO    turnoDAO    = new TurnoDAO();

    // Arreglo fijo de especialidades disponibles en la clinica
    private static final String[] ESPECIALIDADES = {
        "Clinica Medica", "Pediatria", "Cardiologia", "Dermatologia",
        "Ginecologia", "Traumatologia", "Oftalmologia", "Odontologia"
    };

    // -- Pacientes --

    public void registrarPaciente(Paciente paciente) {
        try {
            if (pacienteDAO.buscarPorDni(paciente.getDni()) != null)
                throw new DniDuplicadoException(paciente.getDni());
            paciente.setId(pacienteDAO.proximoId());
            pacienteDAO.insertar(paciente);
            LogOperaciones.registrar("ALTA PACIENTE: " + paciente.getNombreCompleto() + " DNI:" + paciente.getDni());
            System.out.println("Paciente registrado: " + paciente.getNombreCompleto());
        } catch (SQLException e) {
            throw new ConexionException("Error al registrar paciente", e);
        }
    }

    public Paciente buscarPacientePorDni(String dni) {
        try {
            Paciente p = pacienteDAO.buscarPorDni(dni);
            if (p == null) throw new PacienteNoEncontradoException("DNI: " + dni);
            return p;
        } catch (SQLException e) {
            throw new ConexionException("Error al buscar paciente", e);
        }
    }

    public ArrayList<Paciente> listarPacientesOrdenados() {
        try {
            ArrayList<Paciente> lista = pacienteDAO.listarTodos();
            Ordenamiento.ordenarPorApellido(lista);
            return lista;
        } catch (SQLException e) {
            throw new ConexionException("Error al listar pacientes", e);
        }
    }

    public ArrayList<Paciente> buscarPacientesPorNombre(String texto) {
        try {
            return pacienteDAO.buscarPorNombre(texto);
        } catch (SQLException e) {
            throw new ConexionException("Error en busqueda de pacientes", e);
        }
    }

    // -- Turnos --

    public void asignarTurno(int idPaciente, int idMedico,
                             LocalDateTime fechaHora, String motivo) {
        try {
            if (turnoDAO.horarioOcupado(idMedico, fechaHora)) {
                Medico m = medicoDAO.buscarPorId(idMedico);
                String nombre = m != null ? m.getNombreCompleto() : "ID:" + idMedico;
                throw new HorarioOcupadoException(nombre, fechaHora.toString());
            }
            Turno t = new Turno(turnoDAO.proximoId(), idPaciente, idMedico,
                                fechaHora, motivo, Turno.Estado.ACTIVO);
            turnoDAO.insertar(t);
            LogOperaciones.registrar("ASIGNACION TURNO #" + t.getIdTurno()
                + " Paciente:" + idPaciente + " Medico:" + idMedico + " " + fechaHora);
            System.out.println("Turno #" + t.getIdTurno() + " asignado correctamente.");
        } catch (SQLException e) {
            throw new ConexionException("Error al asignar turno", e);
        }
    }

    public void cancelarTurno(int idTurno) {
        try {
            Turno t = turnoDAO.buscarPorId(idTurno);
            if (t == null) { System.out.println("No se encontro el turno #" + idTurno); return; }
            if (t.getEstado() != Turno.Estado.ACTIVO) {
                System.out.println("El turno #" + idTurno + " no esta activo [" + t.getEstado() + "]"); return;
            }
            turnoDAO.cancelar(idTurno);
            LogOperaciones.registrar("CANCELACION TURNO #" + idTurno);
            System.out.println("Turno #" + idTurno + " cancelado.");
        } catch (SQLException e) {
            throw new ConexionException("Error al cancelar turno", e);
        }
    }

    public void atenderTurno(int idTurno, String diagnostico, String tratamiento) {
        try {
            Turno t = turnoDAO.buscarPorId(idTurno);
            if (t == null) { System.out.println("No se encontro el turno #" + idTurno); return; }
            if (t.getEstado() != Turno.Estado.ACTIVO) {
                System.out.println("El turno #" + idTurno + " no esta activo."); return;
            }
            turnoDAO.atender(idTurno, diagnostico, tratamiento);
            LogOperaciones.registrar("ATENCION TURNO #" + idTurno + " Dx:" + diagnostico);
            System.out.println("Turno #" + idTurno + " registrado como atendido.");
        } catch (SQLException e) {
            throw new ConexionException("Error al registrar atencion", e);
        }
    }

    public ArrayList<Turno> listarTodosLosTurnos() {
        try { return turnoDAO.listarTodos(); }
        catch (SQLException e) { throw new ConexionException("Error al listar turnos", e); }
    }

    public ArrayList<Turno> listarHistorialPaciente(int idPaciente) {
        try { return turnoDAO.listarPorPaciente(idPaciente); }
        catch (SQLException e) { throw new ConexionException("Error al obtener historial", e); }
    }

    public ArrayList<Turno> listarAgendaDia(int idMedico, LocalDate fecha) {
        try { return turnoDAO.listarActivosPorMedico(idMedico, fecha); }
        catch (SQLException e) { throw new ConexionException("Error al obtener agenda", e); }
    }

    // -- Medicos --

    public ArrayList<Medico> listarMedicos() {
        try { return medicoDAO.listarTodos(); }
        catch (SQLException e) { throw new ConexionException("Error al listar medicos", e); }
    }

    // -- Reportes con arreglo --

    /**
     * Devuelve estadisticas de turnos usando un arreglo fijo de 3 posiciones.
     * [0]=Activos, [1]=Atendidos, [2]=Cancelados
     */
    public int[] obtenerEstadisticas() {
        try { return turnoDAO.contarPorEstado(); }
        catch (SQLException e) { throw new ConexionException("Error al obtener estadisticas", e); }
    }

    /** Devuelve el arreglo fijo de especialidades disponibles en la clinica. */
    public String[] getEspecialidades() { return ESPECIALIDADES; }
}
