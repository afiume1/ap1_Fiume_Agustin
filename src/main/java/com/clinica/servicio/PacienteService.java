package com.clinica.servicio;

import com.clinica.repositorio.PacienteRepositorio;
import com.clinica.modelo.Paciente;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de lógica de negocios para la entidad Paciente.
 * Valida las reglas del negocio antes de delegar al DAO.
 *
 * @author Fiume, Agustín - VINF016173
 */
public class PacienteService {

    private final PacienteRepositorio repositorio = new PacienteRepositorio();

    /**
     * Registra un nuevo paciente aplicando las validaciones de negocio.
     * Verifica que el DNI no esté registrado previamente (RF01).
     *
     * @param paciente el paciente a registrar
     * @throws IllegalArgumentException si los datos son inválidos o el DNI ya existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void registrar(Paciente paciente) throws SQLException {
        // Validaciones básicas
        if (paciente.getNombre() == null || paciente.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del paciente es obligatorio.");
        if (paciente.getApellido() == null || paciente.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido del paciente es obligatorio.");
        if (paciente.getDni() == null || paciente.getDni().isBlank())
            throw new IllegalArgumentException("El DNI del paciente es obligatorio.");
        if (paciente.getFechaNacimiento() == null)
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");

        // Verificar que el DNI no esté repetido (RF01 - flujo alternativo 4a)
        Paciente existente = repositorio.buscarPorDni(paciente.getDni());
        if (existente != null) {
            throw new IllegalArgumentException(
                "Ya existe un paciente registrado con el DNI " + paciente.getDni() +
                ": " + existente.getNombreCompleto()
            );
        }

        // Asignar ID automáticamente
        paciente.setIdPaciente(repositorio.proximoId());

        repositorio.insertar(paciente);
    }

    /**
     * Busca pacientes por nombre o apellido.
     *
     * @param texto fragmento a buscar
     * @return lista de pacientes que coinciden
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Paciente> buscar(String texto) throws SQLException {
        if (texto == null || texto.isBlank())
            return repositorio.listarTodos();
        return repositorio.buscarPorNombre(texto);
    }

    /**
     * Busca un paciente exacto por DNI.
     *
     * @param dni el DNI a buscar
     * @return el paciente, o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    public Paciente buscarPorDni(String dni) throws SQLException {
        return repositorio.buscarPorDni(dni);
    }

    /**
     * Lista todos los pacientes registrados.
     *
     * @return lista completa de pacientes
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Paciente> listarTodos() throws SQLException {
        return repositorio.listarTodos();
    }
}
