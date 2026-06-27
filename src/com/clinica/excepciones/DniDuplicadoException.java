package com.clinica.excepciones;
/** Se lanza cuando se intenta registrar un DNI ya existente (RF01 flujo 5a). */
public class DniDuplicadoException extends RuntimeException {
    public DniDuplicadoException(String dni) {
        super("Ya existe un paciente con el DNI: " + dni);
    }
}
