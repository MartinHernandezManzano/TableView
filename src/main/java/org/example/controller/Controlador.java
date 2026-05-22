package org.example.controller;

import org.example.model.Empleado;
import org.example.util.Conexion;

import java.sql.*;
import java.util.*;

// Controlador — hace las consultas JDBC y devuelve listas de objetos Empleado
// La vista no toca SQL, solo llama a estos métodos
public class Controlador {

    public List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM empleado ORDER BY ID")) {

            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("salario")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
        }

        return empleados;
    }

    //Extensión para el ejercicio 24
    // INSERT — inserta un empleado nuevo en la base de datos
    public void insertar(String nombre, double salario) {
        String sql = "INSERT INTO empleado (id, nombre, salario) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, obtenerSiguienteId()); // ID generado automáticamente
            ps.setString(2, nombre);
            ps.setDouble(3, salario);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    // UPDATE — actualiza nombre y salario del empleado con ese id
    public void actualizar(int id, String nombre, double salario) {
        String sql = "UPDATE empleado SET nombre=?, salario=? WHERE id=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, salario);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    // DELETE — elimina el empleado con ese id
    public void eliminar(int id) {
        String sql = "DELETE FROM empleado WHERE id=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
    // obtiene el siguiente ID disponible sumando 1 al máximo actual
    private int obtenerSiguienteId() throws Exception {
        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT NVL(MAX(id),0)+1 FROM empleado")) {
            rs.next();
            return rs.getInt(1);
        }
    }
}