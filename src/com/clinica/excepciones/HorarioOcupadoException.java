package com.clinica.excepciones;
/** Se lanza cuando el medico ya tiene turno activo en ese horario (CU-09). */
public class HorarioOcupadoException extends RuntimeException {
    public HorarioOcupadoException(String medico, String fechaHora) {
        super("El medico " + medico + " ya tiene turno el " + fechaHora);
    }
}
