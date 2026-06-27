package com.clinica.dao;
import com.clinica.modelo.Medico;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO para la entidad Medico. Implementa IRepositorio (patron DAO).
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class MedicoDAO implements IRepositorio<Medico> {

    @Override
    public void insertar(Medico m) throws SQLException {
        // Simplificado: insertar en Profesional
        String sql = "INSERT INTO Profesional (id_profesional, nombre, apellido, matricula, id_especialidad) VALUES (?,?,?,?,1)";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getId()); ps.setString(2, m.getNombre());
            ps.setString(3, m.getApellido()); ps.setString(4, m.getMatricula());
            ps.executeUpdate();
        }
    }

    @Override
    public Medico buscarPorId(int id) throws SQLException {
        String sql = "SELECT pr.*, e.nombre_especialidad FROM Profesional pr " +
                     "JOIN Especialidad e ON pr.id_especialidad = e.id_especialidad " +
                     "WHERE pr.id_profesional = ?";
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
    public ArrayList<Medico> listarTodos() throws SQLException {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT pr.*, e.nombre_especialidad FROM Profesional pr " +
                     "JOIN Especialidad e ON pr.id_especialidad = e.id_especialidad " +
                     "ORDER BY pr.apellido";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Medico mapear(ResultSet rs) throws SQLException {
        return new Medico(
            rs.getInt("id_profesional"), rs.getString("nombre"), rs.getString("apellido"),
            rs.getString("matricula"), null, null, rs.getString("matricula"),
            rs.getString("nombre_especialidad"), "Consultar agenda"
        );
    }
}
