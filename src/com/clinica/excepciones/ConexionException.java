package com.clinica.excepciones;
/** Se lanza cuando falla la conexion a la base de datos MySQL. */
public class ConexionException extends RuntimeException {
    public ConexionException(String mensaje, Throwable causa) {
        super("Error de conexion a MySQL: " + mensaje, causa);
    }
}
