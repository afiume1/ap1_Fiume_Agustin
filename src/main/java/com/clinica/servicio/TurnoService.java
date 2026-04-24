package com.clinica.servicio;

import com.clinica.repositorio.TurnoRepositorio;
import com.clinica.modelo.Turno;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Capa de lógica de negocios para la entidad Turno.
 * Valida las reglas del negocio antes de delegar al DAO.
 *
 * @author Fiume, Agustín - VINF016173
 */
public class TurnoService {

    private final TurnoRepositorio repositorio = new TurnoRepositorio();

    /**
     * Asigna un nuevo turno verificando que el horario esté disponible (RF02).
     *
     * @param idPaciente     ID del paciente
     * @param idProfesional  ID del profesional
     * @param fechaHora      fecha y hora del turno
     * @param motivo         motivo de la consulta
     * @throws IllegalArgumentException si el horario ya está ocupado
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void asignar(int idPaciente, int idProfesional,
                        LocalDateTime fechaHora, String motivo) throws SQLException {

        // Verificar disponibilidad (RF02 - flujo alternativo 5a)
        if (repositorio.horarioOcupado(idProfesional, fechaHora)) {
            throw new IllegalArgumentException(
                "El profesional ya tiene un turno asignado el " +
                fechaHora.toLocalDate() + " a las " + fechaHora.toLocalTime() +
                ". Por favor seleccioná otro horario."
            );
        }

        Turno turno = new Turno();
        turno.setIdTurno(repositorio.proximoId());
        turno.setIdPaciente(idPaciente);
        turno.setIdProfesional(idProfesional);
        turno.setFechaHora(fechaHora);
        turno.setEstado(Turno.Estado.Activo);
        turno.setMotivoConsulta(motivo);

        repositorio.insertar(turno);
    }

    /**
     * Cancela un turno existente (RF03).
     *
     * @param idTurno el ID del turno a cancelar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cancelar(int idTurno) throws SQLException {
        repositorio.cancelar(idTurno);
    }

    /**
     * Devuelve los turnos de un profesional para una fecha dada.
     * Se usa para mostrar la agenda del día (RF02, RF05).
     *
     * @param idProfesional el ID del profesional
     * @param fecha         la fecha a consultar
     * @return lista de turnos del día
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> obtenerAgendaDia(int idProfesional, LocalDate fecha) throws SQLException {
        return repositorio.listarPorProfesionalYFecha(idProfesional, fecha);
    }

    /**
     * Devuelve el historial de turnos de un paciente (RF05).
     *
     * @param idPaciente el ID del paciente
     * @return lista de turnos ordenados por fecha descendente
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> obtenerHistorial(int idPaciente) throws SQLException {
        return repositorio.listarPorPaciente(idPaciente);
    }
}
