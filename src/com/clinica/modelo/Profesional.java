package com.clinica.modelo;

/**
 * Clase abstracta - segundo nivel de jerarquia Persona -> Profesional.
 * ABSTRACCION: define getFuncion() que implementan Medico y Administrativo.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public abstract class Profesional extends Persona {
    private String matricula;
    private String especialidad;

    public Profesional(int id, String nombre, String apellido, String dni,
                       String telefono, String email, String matricula, String especialidad) {
        super(id, nombre, apellido, dni, telefono, email);
        this.matricula = matricula; this.especialidad = especialidad;
    }

    public abstract String getFuncion();

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("  Matricula : " + matricula);
        System.out.println("  Especialid: " + especialidad);
        System.out.println("  Funcion   : " + getFuncion());
    }

    public String getMatricula()            { return matricula; }
    public void   setMatricula(String m)    { this.matricula = m; }
    public String getEspecialidad()         { return especialidad; }
    public void   setEspecialidad(String e) { this.especialidad = e; }
}
