package com.clinica.dao;
import com.clinica.modelo.Paciente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DAO para la entidad Paciente. Implementa IRepositorio (patron DAO).
 * Ejecuta todas las consultas SQL sobre la tabla Paciente.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class PacienteDAO implements IRepositorio<Paciente> {

    @Override
    public void insertar(Paciente p) throws SQLException {
        String sql = "INSERT INTO Paciente (id_paciente, nombre, apellido, dni, " +
                     "fecha_nacimiento, telefono, email, id_obra_social) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setString(4, p.getDni());
            ps.setDate(5, Date.valueOf(p.getFechaNacimiento()));
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getEmail());
            ps.setInt(8, 4); // Por defecto: Particular
            ps.executeUpdate();
        }
    }

    @Override
    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE id_paciente = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Paciente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE dni = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Paciente> listarTodos() throws SQLException {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT p.*, os.nombre as nombre_os FROM Paciente p " +
                     "LEFT JOIN ObraSocial os ON p.id_obra_social = os.id_obra_social " +
                     "ORDER BY p.apellido, p.nombre";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Paciente p = mapear(rs);
                try { p.setObraSocial(rs.getString("nombre_os")); } catch (Exception ignored) {}
                lista.add(p);
            }
        }
        return lista;
    }

    public ArrayList<Paciente> buscarPorNombre(String texto) throws SQLException {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente WHERE LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? ORDER BY apellido";
        String patron = "%" + texto.toLowerCase() + "%";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron); ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public int proximoId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_paciente), 0) + 1 FROM Paciente";
        try (Connection con = Conexion.getInstancia().getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente(
            rs.getInt("id_paciente"), rs.getString("nombre"), rs.getString("apellido"),
            rs.getString("dni"), rs.getString("telefono"), rs.getString("email"),
            rs.getDate("fecha_nacimiento").toLocalDate(), null
        );
        return p;
    }
}
