package com.clinica.modelo;

import java.time.LocalDate;

/**
 * Clase que representa a un paciente de la clínica.
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class Paciente {

    private int       idPaciente;
    private String    nombre;
    private String    apellido;
    private String    dni;
    private LocalDate fechaNacimiento;
    private String    telefono;
    private String    email;
    private int       idObraSocial;

    public Paciente() {}

    public Paciente(int idPaciente, String nombre, String apellido, String dni,
                    LocalDate fechaNacimiento, String telefono, String email, int idObraSocial) {
        this.idPaciente      = idPaciente;
        this.nombre          = nombre;
        this.apellido        = apellido;
        this.dni             = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono        = telefono;
        this.email           = email;
        this.idObraSocial    = idObraSocial;
    }

    public int       getIdPaciente()              { return idPaciente; }
    public void      setIdPaciente(int id)        { this.idPaciente = id; }
    public String    getNombre()                  { return nombre; }
    public void      setNombre(String nombre)     { this.nombre = nombre; }
    public String    getApellido()                { return apellido; }
    public void      setApellido(String apellido) { this.apellido = apellido; }
    public String    getDni()                     { return dni; }
    public void      setDni(String dni)           { this.dni = dni; }
    public LocalDate getFechaNacimiento()                    { return fechaNacimiento; }
    public void      setFechaNacimiento(LocalDate fecha)     { this.fechaNacimiento = fecha; }
    public String    getTelefono()                { return telefono; }
    public void      setTelefono(String tel)      { this.telefono = tel; }
    public String    getEmail()                   { return email; }
    public void      setEmail(String email)       { this.email = email; }
    public int       getIdObraSocial()            { return idObraSocial; }
    public void      setIdObraSocial(int id)      { this.idObraSocial = id; }

    /** Nombre completo para mostrar en la UI: "Apellido, Nombre" */
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (DNI: " + dni + ")";
    }
}
