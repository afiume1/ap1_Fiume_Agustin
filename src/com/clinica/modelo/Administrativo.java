package com.clinica.modelo;

/**
 * Tercer nivel: Persona -> Profesional -> Administrativo.
 * POLIMORFISMO: junto a Medico demuestra polimorfismo sobre Profesional.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Administrativo extends Profesional {
    private String sector;
    private int    turnosGestionados;

    public Administrativo(int id, String nombre, String apellido, String dni,
                          String telefono, String email, String matricula, String sector) {
        super(id, nombre, apellido, dni, telefono, email, matricula, sector);
        this.sector = sector; this.turnosGestionados = 0;
    }

    @Override public String getRol()     { return "Administrativo"; }
    @Override public String getFuncion() { return "Gestion de turnos, recepcion de pacientes y facturacion"; }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("  Sector      : " + sector);
        System.out.println("  Turnos gest.: " + turnosGestionados);
    }

    public void   registrarGestion()     { this.turnosGestionados++; }
    public String getSector()            { return sector; }
    public void   setSector(String s)    { this.sector = s; }
    public int    getTurnosGestionados() { return turnosGestionados; }
}
