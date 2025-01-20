package org.example;

import java.sql.*;

public class GestorBaseDatos {
    private static final String DB_URL = "jdbc:sqlite:gastos_ingresos.db";

    public GestorBaseDatos() {
        crearBaseDatos();
    }

    private void crearBaseDatos() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String crearTablaSQL = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dni TEXT NOT NULL UNIQUE)";
            stmt.execute(crearTablaSQL);
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos: " + e.getMessage());
        }
    }

    public boolean agregarUsuario(String dni) {
        String insertarSQL = "INSERT INTO usuarios (dni) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertarSQL)) {
            pstmt.setString(1, dni);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean usuarioExiste(String dni) {
        String consultarSQL = "SELECT * FROM usuarios WHERE dni = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(consultarSQL)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario: " + e.getMessage());
            return false;
        }
    }
}


