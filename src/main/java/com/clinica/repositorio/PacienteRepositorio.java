package com.clinica.repositorio;

import com.clinica.modelo.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la entidad Paciente.
 * Contiene todos los métodos de acceso a la tabla Paciente en MySQL.
 * Es la única clase del sistema que conoce la estructura de esa tabla.
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class PacienteRepositorio {

    /**
     * Inserta un nuevo paciente en la base de datos.
     *
     * @param paciente el paciente a registrar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void insertar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO Paciente " +
                     "(id_paciente, nombre, apellido, dni, fecha_nacimiento, " +
                     " telefono, email, id_obra_social) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paciente.getIdPaciente());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getDni());
            ps.setDate(5, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(6, paciente.getTelefono());
            ps.setString(7, paciente.getEmail());
            ps.setInt(8, paciente.getIdObraSocial());
            ps.executeUpdate();
        }
    }

    /**
     * Busca un paciente por su DNI. Retorna null si no existe.
     * Se usa para la validación de duplicados del RF01.
     *
     * @param dni el DNI a buscar
     * @return el Paciente encontrado, o null
     * @throws SQLException si ocurre un error en la base de datos
     */
    public Paciente buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM Paciente WHERE dni = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /**
     * Busca pacientes cuyo nombre o apellido contenga el texto dado.
     *
     * @param texto fragmento de nombre o apellido
     * @return lista de pacientes que coinciden, ordenada alfabéticamente
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Paciente> buscarPorNombre(String texto) throws SQLException {
        String sql = "SELECT * FROM Paciente " +
                     "WHERE LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? " +
                     "ORDER BY apellido, nombre";
        String patron = "%" + texto.toLowerCase() + "%";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Devuelve todos los pacientes registrados ordenados alfabéticamente.
     *
     * @return lista completa de pacientes
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Paciente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Paciente ORDER BY apellido, nombre";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /**
     * Devuelve el próximo ID disponible para un nuevo paciente.
     *
     * @return el próximo ID
     * @throws SQLException si ocurre un error en la base de datos
     */
    public int proximoId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(id_paciente), 0) + 1 FROM Paciente";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    /** Mapea un ResultSet a un objeto Paciente. */
    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setIdPaciente(rs.getInt("id_paciente"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setDni(rs.getString("dni"));
        p.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setIdObraSocial(rs.getInt("id_obra_social"));
        return p;
    }
}
