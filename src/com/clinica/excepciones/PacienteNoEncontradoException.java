package com.clinica.excepciones;
/** Excepcion lanzada cuando no se encuentra un paciente con el criterio dado. */
public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(String criterio) {
        super("No se encontro ningun paciente con el criterio: " + criterio);
    }
}
