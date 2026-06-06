package com.clinica.modelo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un turno medico.
 * ENCAPSULAMIENTO: el estado solo cambia a traves de atender() y cancelar().
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Turno {
    public enum Estado {
        ACTIVO, ATENDIDO, CANCELADO;
        @Override public String toString() {
            switch(this){case ACTIVO:return "Activo";case ATENDIDO:return "Atendido";default:return "Cancelado";}
        }
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int           idTurno;
    private Paciente      paciente;
    private Medico        medico;
    private LocalDateTime fechaHora;
    private Estado        estado;
    private String        motivoConsulta;
    private String        diagnostico;
    private String        tratamiento;

    public Turno(int idTurno, Paciente paciente, Medico medico,
                 LocalDateTime fechaHora, String motivoConsulta) {
        this.idTurno = idTurno; this.paciente = paciente; this.medico = medico;
        this.fechaHora = fechaHora; this.motivoConsulta = motivoConsulta;
        this.estado = Estado.ACTIVO;
    }

    /** Registra la atencion y actualiza contadores. */
    public void atender(String diagnostico, String tratamiento) {
        this.diagnostico = diagnostico; this.tratamiento = tratamiento;
        this.estado = Estado.ATENDIDO;
        this.medico.registrarConsulta();
        this.paciente.incrementarTurnos();
    }

    /** Cancela el turno de forma logica. */
    public void cancelar() {
        this.estado = Estado.CANCELADO;
        this.paciente.decrementarTurnos();
    }

    public void mostrarInfo() {
        System.out.println("  Turno #   : " + idTurno);
        System.out.println("  Paciente  : " + paciente.getNombreCompleto());
        System.out.println("  Medico    : " + medico.getNombreCompleto() + " (" + medico.getEspecialidad() + ")");
        System.out.println("  Fecha/Hora: " + fechaHora.format(FMT));
        System.out.println("  Motivo    : " + motivoConsulta);
        System.out.println("  Estado    : " + estado);
        if (estado == Estado.ATENDIDO) {
            System.out.println("  Diagnostico: " + diagnostico);
            System.out.println("  Tratamiento: " + tratamiento);
        }
    }

    public int           getIdTurno()                   { return idTurno; }
    public Paciente      getPaciente()                  { return paciente; }
    public Medico        getMedico()                    { return medico; }
    public LocalDateTime getFechaHora()                 { return fechaHora; }
    public void          setFechaHora(LocalDateTime fh) { this.fechaHora = fh; }
    public Estado        getEstado()                    { return estado; }
    public String        getMotivoConsulta()            { return motivoConsulta; }
    public String        getDiagnostico()               { return diagnostico; }
    public String        getTratamiento()               { return tratamiento; }

    @Override
    public String toString() {
        return "Turno #" + idTurno + " | " + fechaHora.format(FMT)
             + " | " + paciente.getNombreCompleto() + " -> " + medico.getNombreCompleto()
             + " [" + estado + "]";
    }
}
