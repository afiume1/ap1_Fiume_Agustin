package com.clinica.dao;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaz generica para el patron DAO/Repositorio.
 * Define las operaciones CRUD que todo repositorio debe implementar.
 * Favorece la reutilizacion de metodos (uso de interfaces segun consigna).
 *
 * @param <T> el tipo de entidad que maneja el repositorio
 * @author Fiume, Agustin Nicolas - VINF016173
 */
public interface IRepositorio<T> {
    void insertar(T entidad) throws SQLException;
    T    buscarPorId(int id) throws SQLException;
    ArrayList<T> listarTodos() throws SQLException;
}
