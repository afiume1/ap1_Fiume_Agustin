package com.clinica.excepciones;
/** Excepcion lanzada cuando el medico ya tiene turno en ese horario (CU-09). */
public class HorarioOcupadoException extends RuntimeException {
    public HorarioOcupadoException(String medico, String fechaHora) {
        super("El medico " + medico + " ya tiene un turno asignado el " + fechaHora);
    }
}
