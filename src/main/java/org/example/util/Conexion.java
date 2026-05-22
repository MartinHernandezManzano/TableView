package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class Conexion {

    public static Connection getConnection() throws SQLException {

        Properties props = new Properties();

        // Busca el fichero db.properties dentro de src/main/resources/
        InputStream input = Conexion.class
                .getClassLoader()
                .getResourceAsStream("db.properties");

        try {
            props.load(input);  // Carga las claves y valores del fichero
        } catch (IOException e) {
            throw new SQLException("No se pudo leer db.properties: " + e.getMessage());
        }

        String url      = props.getProperty("db.url");
        String usuario  = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, usuario, password);
    }

    //comprobamos
    public static void main(String[] args) {
        try (Connection conn = Conexion.getConnection()) {
            System.out.println("Conexión exitosa!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}