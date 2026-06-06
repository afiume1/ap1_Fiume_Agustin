package com.clinica.modelo;

/**
 * Tercer nivel de jerarquia: Persona -> Profesional -> Medico.
 * POLIMORFISMO: sobreescribe getRol(), getFuncion() y mostrarInfo().
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
    @Override public String getFuncion() { return "Atencion de pacientes y registro de consultas medicas"; }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("  Horario   : " + horarioAtencion);
        System.out.println("  Consultas : " + consultasAtendidas + " atendidas");
    }

    public void   registrarConsulta()              { this.consultasAtendidas++; }
    public String getHorarioAtencion()             { return horarioAtencion; }
    public void   setHorarioAtencion(String h)     { this.horarioAtencion = h; }
    public int    getConsultasAtendidas()          { return consultasAtendidas; }
}
