package com.clinica.dao;
import com.clinica.modelo.Turno;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DAO para la entidad Turno. Implementa IRepositorio (patron DAO).
 * Contiene las consultas SQL del sistema: insercion, cancelacion,
 * verificacion de disponibilidad e historial.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class TurnoDAO implements IRepositorio<Turno> {

    @Override
    public void insertar(Turno t) throws SQLException {
        String sql = "INSERT INTO Turno (id_turno, id_paciente, id_profesional, " +
                     "fecha_hora, estado, motivo_consulta) VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getIdTurno()); ps.setInt(2, t.getIdPaciente());
            ps.setInt(3, t.getIdMedico());
            ps.setTimestamp(4, Timestamp.valueOf(t.getFechaHora()));
            ps.setString(5, t.getEstado().name()); ps.setString(6, t.getMotivoConsulta());
            ps.executeUpdate();
        }
    }

    @Override
    public Turno buscarPorId(int id) throws SQLException {
        String sql = buildSelectConJoins() + " WHERE t.id_turno = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Turno> listarTodos() throws SQLException {
        ArrayList<Turno> lista = new ArrayList<>();
        String sql = buildSelectConJoins() + " ORDER BY t.fecha_hora DESC";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /** Verifica si un medico ya tiene turno activo en esa fecha/hora (CU-09). */
    public boolean horarioOcupado(int idMedico, java.time.LocalDateTime fechaHora) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Turno WHERE id_profesional = ? AND fecha_hora = ? AND estado = 'ACTIVO'";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedico); ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /** Cancela un turno de forma logica (borrado logico). */
    public void cancelar(int idTurno) throws SQLException {
        String sql = "UPDATE Turno SET estado = 'CANCELADO', fecha_cancelacion = ? WHERE id_turno = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now())); ps.setInt(2, idTurno);
            ps.executeUpdate();
        }
    }

    /** Registra atencion: diagnostico, tratamiento y cambia estado a ATENDIDO. */
    public void atender(int idTurno, String diagnostico, String tratamiento) throws SQLException {
        // Primero actualizar el turno
        String sqlTurno = "UPDATE Turno SET estado = 'ATENDIDO' WHERE id_turno = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sqlTurno)) {
            ps.setInt(1, idTurno); ps.executeUpdate();
        }
        // Luego insertar la consulta
        String sqlConsulta = "INSERT INTO Consulta (id_consulta, id_turno, diagnostico, tratamiento, fecha_consulta) " +
                             "SELECT COALESCE(MAX(id_consulta),0)+1, ?, ?, ?, CURDATE() FROM Consulta";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sqlConsulta)) {
            ps.setInt(1, idTurno); ps.setString(2, diagnostico); ps.setString(3, tratamiento);
            ps.executeUpdate();
        }
    }

    /** Historial de turnos de un paciente ordenado por fecha descendente (RF05). */
    public ArrayList<Turno> listarPorPaciente(int idPaciente) throws SQLException {
        ArrayList<Turno> lista = new ArrayList<>();
        String sql = buildSelectConJoins() + " WHERE t.id_paciente = ? ORDER BY t.fecha_hora DESC";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Turnos activos de un medico para una fecha (agenda del dia). */
    public ArrayList<Turno> listarActivosPorMedico(int idMedico, LocalDate fecha) throws SQLException {
        ArrayList<Turno> lista = new ArrayList<>();
        String sql = buildSelectConJoins() +
                     " WHERE t.id_profesional = ? AND DATE(t.fecha_hora) = ? AND t.estado = 'ACTIVO'" +
                     " ORDER BY t.fecha_hora";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedico); ps.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public int proximoId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_turno), 0) + 1 FROM Turno";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    /** Reporte: cantidad de turnos por estado (usa arreglo fijo de 3 elementos). */
    public int[] contarPorEstado() throws SQLException {
        // Arreglo fijo: [0]=ACTIVO, [1]=ATENDIDO, [2]=CANCELADO
        int[] contadores = new int[3];
        String sql = "SELECT estado, COUNT(*) AS total FROM Turno GROUP BY estado";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String estado = rs.getString("estado");
                int total = rs.getInt("total");
                if ("ACTIVO".equals(estado))    contadores[0] = total;
                if ("ATENDIDO".equals(estado))  contadores[1] = total;
                if ("CANCELADO".equals(estado)) contadores[2] = total;
            }
        }
        return contadores;
    }

    private String buildSelectConJoins() {
        return "SELECT t.*, " +
               "CONCAT(p.apellido, ', ', p.nombre) AS nombre_paciente, " +
               "CONCAT(pr.apellido, ', ', pr.nombre) AS nombre_medico, " +
               "e.nombre_especialidad, c.diagnostico, c.tratamiento " +
               "FROM Turno t " +
               "JOIN Paciente p ON t.id_paciente = p.id_paciente " +
               "JOIN Profesional pr ON t.id_profesional = pr.id_profesional " +
               "JOIN Especialidad e ON pr.id_especialidad = e.id_especialidad " +
               "LEFT JOIN Consulta c ON c.id_turno = t.id_turno";
    }

    private Turno mapear(ResultSet rs) throws SQLException {
        Turno t = new Turno(
            rs.getInt("id_turno"), rs.getInt("id_paciente"), rs.getInt("id_profesional"),
            rs.getTimestamp("fecha_hora").toLocalDateTime(),
            rs.getString("motivo_consulta"),
            Turno.Estado.valueOf(rs.getString("estado"))
        );
        t.setNombrePaciente(rs.getString("nombre_paciente"));
        t.setNombreMedico(rs.getString("nombre_medico"));
        t.setEspecialidad(rs.getString("nombre_especialidad"));
        t.setDiagnostico(rs.getString("diagnostico"));
        t.setTratamiento(rs.getString("tratamiento"));
        Date fc = rs.getDate("fecha_cancelacion");
        if (fc != null) t.setFechaCancelacion(fc.toLocalDate());
        return t;
    }
}
