package com.clinica.modelo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Turno medico. ENCAPSULAMIENTO: estado controlado por metodos.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Turno {
    public enum Estado {
        ACTIVO, ATENDIDO, CANCELADO;
        @Override public String toString() {
            switch(this) { case ACTIVO: return "Activo"; case ATENDIDO: return "Atendido"; default: return "Cancelado"; }
        }
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int           idTurno;
    private int           idPaciente;
    private int           idMedico;
    private String        nombrePaciente;
    private String        nombreMedico;
    private String        especialidad;
    private LocalDateTime fechaHora;
    private Estado        estado;
    private String        motivoConsulta;
    private String        diagnostico;
    private String        tratamiento;
    private LocalDate     fechaCancelacion;

    public Turno(int idTurno, int idPaciente, int idMedico, LocalDateTime fechaHora,
                 String motivoConsulta, Estado estado) {
        this.idTurno = idTurno; this.idPaciente = idPaciente; this.idMedico = idMedico;
        this.fechaHora = fechaHora; this.motivoConsulta = motivoConsulta; this.estado = estado;
    }

    public void mostrarInfo() {
        System.out.println("  Turno #    : " + idTurno);
        System.out.println("  Paciente   : " + (nombrePaciente != null ? nombrePaciente : "ID:" + idPaciente));
        System.out.println("  Medico     : " + (nombreMedico != null ? nombreMedico + " (" + especialidad + ")" : "ID:" + idMedico));
        System.out.println("  Fecha/Hora : " + fechaHora.format(FMT));
        System.out.println("  Motivo     : " + motivoConsulta);
        System.out.println("  Estado     : " + estado);
        if (estado == Estado.ATENDIDO && diagnostico != null) {
            System.out.println("  Diagnostico: " + diagnostico);
            System.out.println("  Tratamiento: " + tratamiento);
        }
        if (estado == Estado.CANCELADO && fechaCancelacion != null) {
            System.out.println("  Cancelado  : " + fechaCancelacion);
        }
    }

    public int           getIdTurno()                    { return idTurno; }
    public void          setIdTurno(int id)              { this.idTurno = id; }
    public int           getIdPaciente()                 { return idPaciente; }
    public void          setIdPaciente(int id)           { this.idPaciente = id; }
    public int           getIdMedico()                   { return idMedico; }
    public void          setIdMedico(int id)             { this.idMedico = id; }
    public String        getNombrePaciente()             { return nombrePaciente; }
    public void          setNombrePaciente(String n)     { this.nombrePaciente = n; }
    public String        getNombreMedico()               { return nombreMedico; }
    public void          setNombreMedico(String n)       { this.nombreMedico = n; }
    public String        getEspecialidad()               { return especialidad; }
    public void          setEspecialidad(String e)       { this.especialidad = e; }
    public LocalDateTime getFechaHora()                  { return fechaHora; }
    public void          setFechaHora(LocalDateTime fh)  { this.fechaHora = fh; }
    public Estado        getEstado()                     { return estado; }
    public void          setEstado(Estado e)             { this.estado = e; }
    public String        getMotivoConsulta()             { return motivoConsulta; }
    public void          setMotivoConsulta(String m)     { this.motivoConsulta = m; }
    public String        getDiagnostico()                { return diagnostico; }
    public void          setDiagnostico(String d)        { this.diagnostico = d; }
    public String        getTratamiento()                { return tratamiento; }
    public void          setTratamiento(String t)        { this.tratamiento = t; }
    public LocalDate     getFechaCancelacion()           { return fechaCancelacion; }
    public void          setFechaCancelacion(LocalDate f){ this.fechaCancelacion = f; }

    @Override
    public String toString() {
        return "Turno #" + idTurno + " | " + fechaHora.format(FMT)
             + " | " + (nombrePaciente != null ? nombrePaciente : "ID:"+idPaciente)
             + " -> " + (nombreMedico != null ? nombreMedico : "ID:"+idMedico)
             + " [" + estado + "]";
    }
}
