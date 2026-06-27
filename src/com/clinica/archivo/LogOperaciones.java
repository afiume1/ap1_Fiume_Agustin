package com.clinica.archivo;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manejo de archivos: registra un log de operaciones del sistema.
 * Cada operacion importante (alta, cancelacion, atencion) queda
 * registrada en el archivo log_operaciones.txt.
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public class LogOperaciones {

    private static final String ARCHIVO = "log_operaciones.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private LogOperaciones() {}

    /**
     * Registra una operacion en el archivo de log.
     * @param operacion descripcion de la operacion realizada
     */
    public static void registrar(String operacion) {
        String linea = "[" + LocalDateTime.now().format(FMT) + "] " + operacion;
        try (FileWriter fw = new FileWriter(ARCHIVO, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Advertencia: no se pudo escribir en el log: " + e.getMessage());
        }
    }

    /**
     * Muestra el contenido del log por pantalla.
     */
    public static void mostrar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("No hay operaciones registradas aun.");
            return;
        }
        System.out.println("\n=== Log de operaciones ===");
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el log: " + e.getMessage());
        }
    }
}
