package com.clinica.excepciones;
/** Se lanza cuando no se encuentra un paciente con el criterio dado. */
public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(String criterio) {
        super("No se encontro ningun paciente: " + criterio);
    }
}
