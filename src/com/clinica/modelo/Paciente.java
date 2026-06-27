package com.clinica.modelo;
import java.time.LocalDate;
import java.time.Period;

/**
 * Paciente de la clinica. HERENCIA de Persona.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Paciente extends Persona {
    private LocalDate fechaNacimiento;
    private String    obraSocial;
    private int       cantidadTurnos;

    public Paciente(int id, String nombre, String apellido, String dni,
                    String telefono, String email, LocalDate fechaNacimiento, String obraSocial) {
        super(id, nombre, apellido, dni, telefono, email);
        this.fechaNacimiento = fechaNacimiento;
        this.obraSocial = obraSocial;
        this.cantidadTurnos = 0;
    }

    @Override public String getRol() { return "Paciente"; }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("  Fecha nac.: " + fechaNacimiento);
        System.out.println("  Edad      : " + calcularEdad() + " años");
        System.out.println("  Obra soc. : " + (obraSocial != null ? obraSocial : "Particular"));
        System.out.println("  Turnos    : " + cantidadTurnos);
    }

    public int calcularEdad() { return Period.between(fechaNacimiento, LocalDate.now()).getYears(); }
    public void incrementarTurnos() { this.cantidadTurnos++; }
    public void decrementarTurnos() { if (cantidadTurnos > 0) cantidadTurnos--; }

    public LocalDate getFechaNacimiento()            { return fechaNacimiento; }
    public void      setFechaNacimiento(LocalDate f) { this.fechaNacimiento = f; }
    public String    getObraSocial()                 { return obraSocial; }
    public void      setObraSocial(String os)        { this.obraSocial = os; }
    public int       getCantidadTurnos()             { return cantidadTurnos; }
    public void      setCantidadTurnos(int n)        { this.cantidadTurnos = n; }
}
