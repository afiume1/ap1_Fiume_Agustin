package com.clinica.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase que representa un turno médico.
 *
 * @author Fiume, Agustín - VINF016173
 */
public class Turno {

    public enum Estado { Activo, Atendido, Cancelado }

    private int idTurno;
    private int idPaciente;
    private int idProfesional;
    private LocalDateTime fechaHora;
    private Estado estado;
    private String motivoConsulta;
    private LocalDate fechaCancelacion;

    // Campos adicionales para mostrar en la UI (se llenan con JOIN)
    private String nombrePaciente;
    private String nombreProfesional;

    public Turno() {}

    public Turno(int idTurno, int idPaciente, int idProfesional,
                 LocalDateTime fechaHora, Estado estado, String motivoConsulta) {
        this.idTurno        = idTurno;
        this.idPaciente     = idPaciente;
        this.idProfesional  = idProfesional;
        this.fechaHora      = fechaHora;
        this.estado         = estado;
        this.motivoConsulta = motivoConsulta;
    }

    // Getters y setters
    public int getIdTurno()                     { return idTurno; }
    public void setIdTurno(int id)              { this.idTurno = id; }

    public int getIdPaciente()                  { return idPaciente; }
    public void setIdPaciente(int id)           { this.idPaciente = id; }

    public int getIdProfesional()               { return idProfesional; }
    public void setIdProfesional(int id)        { this.idProfesional = id; }

    public LocalDateTime getFechaHora()                     { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora)       { this.fechaHora = fechaHora; }

    public Estado getEstado()                   { return estado; }
    public void setEstado(Estado estado)        { this.estado = estado; }

    public String getMotivoConsulta()           { return motivoConsulta; }
    public void setMotivoConsulta(String m)     { this.motivoConsulta = m; }

    public LocalDate getFechaCancelacion()              { return fechaCancelacion; }
    public void setFechaCancelacion(LocalDate fecha)    { this.fechaCancelacion = fecha; }

    public String getNombrePaciente()               { return nombrePaciente; }
    public void setNombrePaciente(String n)         { this.nombrePaciente = n; }

    public String getNombreProfesional()            { return nombreProfesional; }
    public void setNombreProfesional(String n)      { this.nombreProfesional = n; }

    @Override
    public String toString() {
        return "Turno #" + idTurno + " | " + fechaHora + " | " + estado;
    }
}
