package com.clinica.util;
import com.clinica.modelo.Paciente;
import com.clinica.modelo.Turno;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmos de ordenamiento y busqueda.
 * Implementa Bubble Sort para pacientes y busqueda binaria para turnos.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Ordenamiento {
    private Ordenamiento() {}

    /**
     * Ordena pacientes por apellido usando Bubble Sort. O(n^2).
     * Usa estructura repetitiva: dos for anidados.
     */
    public static void ordenarPacientesPorApellido(List<Paciente> pacientes) {
        int n = pacientes.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String ap1 = pacientes.get(j).getApellido().toLowerCase();
                String ap2 = pacientes.get(j + 1).getApellido().toLowerCase();
                if (ap1.compareTo(ap2) > 0) {
                    Paciente temp = pacientes.get(j);
                    pacientes.set(j, pacientes.get(j + 1));
                    pacientes.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Ordena turnos por ID usando Bubble Sort. Necesario antes de busqueda binaria.
     */
    public static void ordenarTurnosPorId(List<Turno> turnos) {
        int n = turnos.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (turnos.get(j).getIdTurno() > turnos.get(j + 1).getIdTurno()) {
                    Turno temp = turnos.get(j);
                    turnos.set(j, turnos.get(j + 1));
                    turnos.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Busca un turno por ID usando busqueda binaria. O(log n).
     * Requiere lista ordenada por ID.
     */
    public static Turno busquedaBinariaTurno(List<Turno> turnos, int idBuscar) {
        int izquierda = 0, derecha = turnos.size() - 1;
        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            int idMedio = turnos.get(medio).getIdTurno();
            if (idMedio == idBuscar)   return turnos.get(medio);
            else if (idMedio < idBuscar) izquierda = medio + 1;
            else                         derecha   = medio - 1;
        }
        return null;
    }

    /**
     * Busqueda lineal de pacientes por nombre o apellido. O(n).
     */
    public static List<Paciente> buscarPacientesPorNombre(List<Paciente> pacientes, String texto) {
        List<Paciente> resultado = new ArrayList<>();
        String t = texto.toLowerCase();
        for (Paciente p : pacientes) {
            if (p.getNombre().toLowerCase().contains(t) || p.getApellido().toLowerCase().contains(t)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
