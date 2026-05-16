package com.clinica.servicio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para calcular el hash SHA-256 de contraseñas.
 * Las contraseñas NUNCA se almacenan en texto plano.
 * Se guarda el hash en la columna password_hash de la tabla Usuario.
 * Esto cumple con el RNF03 definido en el TP2.
 *
 * @author Fiume, Agustín Nicolás - VINF016173
 */
public class HashUtil {

    private HashUtil() {}

    /**
     * Calcula el hash SHA-256 de un texto y lo devuelve como String hexadecimal.
     *
     * @param texto el texto a hashear (la contraseña en texto plano)
     * @return el hash SHA-256 en formato hexadecimal (64 caracteres)
     */
    public static String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 siempre está disponible en Java, esto nunca debería ocurrir
            throw new RuntimeException("Error al calcular el hash SHA-256", e);
        }
    }
}
