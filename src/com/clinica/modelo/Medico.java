package com.clinica.modelo;

/**
 * Medico de la clinica. Tercer nivel de herencia.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Medico extends Profesional {
    private String horarioAtencion;
    private int    consultasAtendidas;

    public Medico(int id, String nombre, String apellido, String dni,
                  String telefono, String email, String matricula,
                  String especialidad, String horarioAtencion) {
        super(id, nombre, apellido, dni, telefono, email, matricula, especialidad);
        this.horarioAtencion = horarioAtencion;
        this.consultasAtendidas = 0;
    }

    @Override public String getRol()     { return "Medico"; }
    @Override public String getFuncion() { return "Atencion de pacientes y registro de consultas"; }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("  Horario   : " + horarioAtencion);
        System.out.println("  Consultas : " + consultasAtendidas + " atendidas");
    }

    public void   registrarConsulta()          { this.consultasAtendidas++; }
    public String getHorarioAtencion()         { return horarioAtencion; }
    public void   setHorarioAtencion(String h) { this.horarioAtencion = h; }
    public int    getConsultasAtendidas()      { return consultasAtendidas; }
    public void   setConsultasAtendidas(int n) { this.consultasAtendidas = n; }
}
