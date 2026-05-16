package com.clinica.servicio;

import com.clinica.modelo.Turno;
import com.clinica.repositorio.TurnoRepositorio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Capa de lógica de negocios para la entidad Turno.
 * Valida las reglas del negocio antes de delegar al repositorio.
 * Implementa la lógica de CU-02 (Asignar Turno) y CU-09 (Verificar Disponibilidad).
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class TurnoService {

    private final TurnoRepositorio repositorio = new TurnoRepositorio();

    /**
     * Asigna un nuevo turno verificando que el horario esté disponible.
     * Implementa CU-02 incluyendo CU-09 (verificación de disponibilidad).
     * Corresponde a RF02.
     *
     * @param idPaciente    ID del paciente
     * @param idProfesional ID del profesional
     * @param fechaHora     fecha y hora del turno
     * @param motivo        motivo de la consulta
     * @throws IllegalArgumentException si el horario ya está ocupado (flujo alternativo 5a)
     * @throws SQLException             si ocurre un error en la base de datos
     */
    public void asignar(int idPaciente, int idProfesional,
                        LocalDateTime fechaHora, String motivo) throws SQLException {

        // CU-09: verificar disponibilidad del horario
        if (repositorio.horarioOcupado(idProfesional, fechaHora)) {
            throw new IllegalArgumentException(
                "El profesional ya tiene un turno asignado el " +
                fechaHora.toLocalDate() + " a las " + fechaHora.toLocalTime() + ". " +
                "Por favor seleccioná otro horario disponible."
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
     * Cancela un turno de forma lógica (RF03).
     * No borra el registro: actualiza el estado a "Cancelado" y registra la fecha.
     *
     * @param idTurno el ID del turno a cancelar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cancelar(int idTurno) throws SQLException {
        repositorio.cancelar(idTurno);
    }

    /**
     * Devuelve los turnos activos de un profesional para una fecha dada (RF07).
     * Se usa para mostrar la agenda del día.
     *
     * @param idProfesional el ID del profesional
     * @param fecha         la fecha a consultar
     * @return lista de turnos del día ordenados por hora
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> obtenerAgendaDia(int idProfesional, LocalDate fecha) throws SQLException {
        return repositorio.listarPorProfesionalYFecha(idProfesional, fecha);
    }

    /**
     * Devuelve el historial completo de turnos de un paciente (RF05).
     *
     * @param idPaciente el ID del paciente
     * @return lista de turnos ordenados por fecha descendente
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> obtenerHistorial(int idPaciente) throws SQLException {
        return repositorio.listarPorPaciente(idPaciente);
    }
}
