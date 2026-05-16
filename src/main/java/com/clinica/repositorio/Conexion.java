package com.clinica.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para reutilizar la misma conexión.
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class Conexion {

    private static final String URL      = "jdbc:mysql://localhost:3306/clinica_turnos"
                                         + "?serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "root";

    private static Connection instancia = null;

    private Conexion() {}

    /**
     * Devuelve la conexión activa. Si no existe o está cerrada, crea una nueva.
     *
     * @return Connection la conexión a la base de datos
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection getConexion() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                    "No se encontró el driver de MySQL. " +
                    "Verificá que mysql-connector-j esté en el classpath.", e);
            }
        }
        return instancia;
    }

    /**
     * Cierra la conexión si está abierta.
     */
    public static void cerrar() {
        if (instancia != null) {
            try {
                if (!instancia.isClosed()) instancia.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
