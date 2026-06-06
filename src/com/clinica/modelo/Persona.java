package com.clinica.modelo;

/**
 * Clase abstracta base que representa a cualquier persona del sistema.
 * ABSTRACCION: no puede instanciarse directamente.
 * ENCAPSULAMIENTO: todos los atributos son privados.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public abstract class Persona {
    private int    id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    public Persona(int id, String nombre, String apellido, String dni, String telefono, String email) {
        this.id = id; this.nombre = nombre; this.apellido = apellido;
        this.dni = dni; this.telefono = telefono; this.email = email;
    }

    /** Metodo abstracto - ABSTRACCION: cada subclase define su rol. */
    public abstract String getRol();

    /** Base para POLIMORFISMO: las subclases lo sobreescriben. */
    public void mostrarInfo() {
        System.out.println("  ID      : " + id);
        System.out.println("  Nombre  : " + apellido + ", " + nombre);
        System.out.println("  DNI     : " + dni);
        System.out.println("  Telefono: " + (telefono != null ? telefono : "-"));
        System.out.println("  Email   : " + (email    != null ? email    : "-"));
        System.out.println("  Rol     : " + getRol());
    }

    // ENCAPSULAMIENTO: acceso controlado mediante getters y setters
    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }
    public String getNombre()           { return nombre; }
    public void   setNombre(String n)   { this.nombre = n; }
    public String getApellido()         { return apellido; }
    public void   setApellido(String a) { this.apellido = a; }
    public String getDni()              { return dni; }
    public void   setDni(String d)      { this.dni = d; }
    public String getTelefono()         { return telefono; }
    public void   setTelefono(String t) { this.telefono = t; }
    public String getEmail()            { return email; }
    public void   setEmail(String e)    { this.email = e; }
    public String getNombreCompleto()   { return apellido + ", " + nombre; }

    @Override
    public String toString() { return "[" + getRol() + "] " + getNombreCompleto() + " (DNI: " + dni + ")"; }
}
