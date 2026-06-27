package com.clinica.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patron de diseno SINGLETON para la conexion a MySQL.
 * Garantiza que exista una unica instancia de conexion en todo el sistema.
 * Implementacion thread-safe con doble verificacion (double-checked locking).
 *
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Conexion {

    // Datos de conexion - ajustar segun el entorno local
    private static final String URL      = "jdbc:mysql://localhost:3306/clinica_turnos"
                                         + "?serverTimezone=America/Argentina/Buenos_Aires"
                                         + "&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "root";

    // Instancia unica - volatile para visibilidad entre hilos
    private static volatile Conexion instancia = null;
    private Connection conexion;

    // Constructor privado: impide instanciacion externa (SINGLETON)
    private Conexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado. Agrega mysql-connector-j al classpath.", e);
        }
    }

    /**
     * Devuelve la unica instancia de Conexion.
     * Si no existe o esta cerrada, crea una nueva.
     * Patron SINGLETON con doble verificacion.
     */
    public static Conexion getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            synchronized (Conexion.class) {
                if (instancia == null || instancia.conexion.isClosed()) {
                    instancia = new Conexion();
                }
            }
        }
        return instancia;
    }

    /** Devuelve el objeto Connection de JDBC. */
    public Connection getConexion() { return conexion; }

    /** Cierra la conexion activa. */
    public static void cerrar() {
        if (instancia != null) {
            try {
                if (!instancia.conexion.isClosed()) instancia.conexion.close();
                instancia = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
