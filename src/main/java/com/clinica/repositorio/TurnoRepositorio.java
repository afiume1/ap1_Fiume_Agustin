package com.clinica.repositorio;

import com.clinica.modelo.Turno;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la entidad Turno.
 * Contiene todos los métodos de acceso a la tabla Turno en MySQL.
 * Las consultas SQL implementadas corresponden a las presentadas
 * en el TP2, sección 13.3.
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class TurnoRepositorio {

    /**
     * Inserta un nuevo turno con estado "Activo".
     *
     * @param turno el turno a registrar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void insertar(Turno turno) throws SQLException {
        String sql = "INSERT INTO Turno " +
                     "(id_turno, id_paciente, id_profesional, fecha_hora, estado, motivo_consulta) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, turno.getIdTurno());
            ps.setInt(2, turno.getIdPaciente());
            ps.setInt(3, turno.getIdProfesional());
            ps.setTimestamp(4, Timestamp.valueOf(turno.getFechaHora()));
            ps.setString(5, turno.getEstado().name());
            ps.setString(6, turno.getMotivoConsulta());
            ps.executeUpdate();
        }
    }

    /**
     * Cancela un turno de forma lógica: actualiza el estado a "Cancelado"
     * y registra la fecha de cancelación. No borra el registro.
     * Corresponde al borrado lógico descrito en el TP2, sección 13.4.
     *
     * @param idTurno el ID del turno a cancelar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void cancelar(int idTurno) throws SQLException {
        String sql = "UPDATE Turno " +
                     "SET estado = 'Cancelado', fecha_cancelacion = ? " +
                     "WHERE id_turno = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, idTurno);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica si un profesional ya tiene un turno activo en una fecha y hora dadas.
     * Implementa la lógica del CU-09 (Verificar disponibilidad).
     * Corresponde a la Consulta 2 del TP2, sección 13.3.
     *
     * @param idProfesional el ID del profesional
     * @param fechaHora     la fecha y hora a verificar
     * @return true si el horario está ocupado, false si está disponible
     * @throws SQLException si ocurre un error en la base de datos
     */
    public boolean horarioOcupado(int idProfesional, LocalDateTime fechaHora) throws SQLException {
        String sql = "SELECT COUNT(*) AS ocupado FROM Turno " +
                     "WHERE id_profesional = ? AND fecha_hora = ? AND estado = 'Activo'";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProfesional);
            ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("ocupado") > 0;
            }
        }
        return false;
    }

    /**
     * Devuelve los turnos activos de un profesional para una fecha dada.
     * Corresponde a la Consulta 1 del TP2, sección 13.3.
     *
     * @param idProfesional el ID del profesional
     * @param fecha         la fecha a consultar
     * @return lista de turnos del día ordenados por hora
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> listarPorProfesionalYFecha(int idProfesional, LocalDate fecha)
            throws SQLException {
        String sql = "SELECT t.*, " +
                     "CONCAT(p.apellido, ', ', p.nombre)  AS nombre_paciente, " +
                     "CONCAT(pr.apellido, ', ', pr.nombre) AS nombre_profesional " +
                     "FROM Turno t " +
                     "JOIN Paciente p     ON t.id_paciente = p.id_paciente " +
                     "JOIN Profesional pr ON t.id_profesional = pr.id_profesional " +
                     "WHERE t.id_profesional = ? AND DATE(t.fecha_hora) = ? " +
                     "AND t.estado = 'Activo' " +
                     "ORDER BY t.fecha_hora";

        List<Turno> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProfesional);
            ps.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Devuelve el historial completo de turnos de un paciente ordenado por fecha descendente.
     * Corresponde a la Consulta 3 del TP2, sección 13.3 (RF05).
     *
     * @param idPaciente el ID del paciente
     * @return lista de turnos del paciente
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Turno> listarPorPaciente(int idPaciente) throws SQLException {
        String sql = "SELECT t.*, " +
                     "CONCAT(p.apellido, ', ', p.nombre)  AS nombre_paciente, " +
                     "CONCAT(pr.apellido, ', ', pr.nombre) AS nombre_profesional " +
                     "FROM Turno t " +
                     "JOIN Paciente p     ON t.id_paciente = p.id_paciente " +
                     "JOIN Profesional pr ON t.id_profesional = pr.id_profesional " +
                     "WHERE t.id_paciente = ? " +
                     "ORDER BY t.fecha_hora DESC";

        List<Turno> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Devuelve el próximo ID disponible para un nuevo turno.
     *
     * @return el próximo ID
     * @throws SQLException si ocurre un error en la base de datos
     */
    public int proximoId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_turno), 0) + 1 FROM Turno";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    /** Mapea un ResultSet a un objeto Turno. */
    private Turno mapear(ResultSet rs) throws SQLException {
        Turno t = new Turno();
        t.setIdTurno(rs.getInt("id_turno"));
        t.setIdPaciente(rs.getInt("id_paciente"));
        t.setIdProfesional(rs.getInt("id_profesional"));
        t.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        t.setEstado(Turno.Estado.valueOf(rs.getString("estado")));
        t.setMotivoConsulta(rs.getString("motivo_consulta"));
        Date fc = rs.getDate("fecha_cancelacion");
        if (fc != null) t.setFechaCancelacion(fc.toLocalDate());
        t.setNombrePaciente(rs.getString("nombre_paciente"));
        t.setNombreProfesional(rs.getString("nombre_profesional"));
        return t;
    }
}
