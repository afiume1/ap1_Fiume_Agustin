package com.clinica.util;
import com.clinica.modelo.Paciente;
import java.util.ArrayList;

/**
 * Algoritmos de ordenamiento y busqueda.
 * Bubble Sort para ArrayList de pacientes.
 * Uso complementario de arreglos en los reportes (ver TurnoDAO.contarPorEstado).
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class Ordenamiento {
    private Ordenamiento() {}

    /**
     * Ordena un ArrayList de pacientes por apellido usando Bubble Sort. O(n^2).
     */
    public static void ordenarPorApellido(ArrayList<Paciente> pacientes) {
        int n = pacientes.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (pacientes.get(j).getApellido().toLowerCase()
                        .compareTo(pacientes.get(j+1).getApellido().toLowerCase()) > 0) {
                    Paciente temp = pacientes.get(j);
                    pacientes.set(j, pacientes.get(j+1));
                    pacientes.set(j+1, temp);
                }
            }
        }
    }

    /**
     * Busqueda lineal de pacientes por nombre o apellido. O(n).
     */
    public static ArrayList<Paciente> buscarPorNombre(ArrayList<Paciente> pacientes, String texto) {
        ArrayList<Paciente> resultado = new ArrayList<>();
        String t = texto.toLowerCase();
        for (Paciente p : pacientes) {
            if (p.getNombre().toLowerCase().contains(t) || p.getApellido().toLowerCase().contains(t)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
