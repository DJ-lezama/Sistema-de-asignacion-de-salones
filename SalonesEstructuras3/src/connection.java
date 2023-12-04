import javax.swing.*;
import java.sql.*;
import java.util.*;

public class connection {
    private static connection instance = null;
    private static final String URL = "jdbc:mysql://localhost:3306/abb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // URL de conexión
    private static final String USER = "root"; // Usuario
    private static final String PASSWORD = "klmn5678";

    public static connection getInstance() {
        if (instance == null) {
            instance = new connection();
        }
        return instance;
    }

    public static Connection getConection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found", e);
        } catch (SQLException e) {
            System.out.println("Connection error");
            throw e; // Rethrow the exception
        }
    }

    public List<String[]> loadDataFromDataBase(){
        List<String[]> data = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Clases; ";
        try (Connection conn = getConection(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] row = new String[11]; // number of columns
                row[0] = rs.getString("codigo");
                row[1] = rs.getString("nombre");
                row[2] = rs.getString("dias");
                row[3] = rs.getString("capacidad");
                row[4] = rs.getString("horaInicio");
                row[5] = rs.getString("duracion");
                row[6] = rs.getString("salon");
                row[7] = rs.getString("edificio");
                row[8] = rs.getString("escuela");
                row[9] = rs.getString("carrera");
                row[10] = rs.getString("semestre");
                data.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("ERROR");
        }
        return data;
    }


    public void insertDataToDatabase(String codigo, String nombre, int dias, int capacidad, String horaInicioDB, int duracion, String salon, String edificio, String escuela, String carrera, int semestre){
        String insertQuery = "INSERT INTO Clases (codigo, nombre, dias, capacidad, horaInicio, duracion, salon, edificio, escuela, carrera, semestre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConection()){
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)){
                pstmt.setString(1, codigo);
                pstmt.setString(2, nombre);
                pstmt.setInt(3, dias);
                pstmt.setInt(4, capacidad);
                pstmt.setString(5, horaInicioDB);
                pstmt.setInt(6, duracion);
                pstmt.setString(7, salon);
                pstmt.setString(8, edificio);
                pstmt.setString(9, escuela);
                pstmt.setString(10, carrera);
                pstmt.setInt(11, semestre);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Clase agregada correctamente a la base de datos.");
                    String message = "Clase agregada correctamente a la base de datos.";
                    JOptionPane.showMessageDialog(null, message);
                } else {
                    System.out.println("Fallo al insertar datos en la base de datos.");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }


    public List<String[]> getSchedule(String sqlQuery, Object[] params){
        List<String[]> data = new ArrayList<>();
        try (Connection conn = getConection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // Set the parameters for the PreparedStatement
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[7]; // number of columns
                row[0] = rs.getString("codigo");
                row[1] = rs.getString("nombre");
                row[2] = rs.getString("dias");
                row[3] = rs.getString("capacidad");
                row[4] = rs.getString("horaInicio");
                row[5] = rs.getString("duracion");
                row[6] = rs.getString("salon");
                data.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("ERROR");
        }
        return data;
    }

    public List<String[]> getData(String sqlQuery, Object[] params){
        List<String[]> data = new ArrayList<>();
        try (Connection conn = getConection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // Set the parameters for the PreparedStatement
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[11]; // number of columns
                row[0] = rs.getString("codigo");
                row[1] = rs.getString("nombre");
                row[2] = rs.getString("dias");
                row[3] = rs.getString("capacidad");
                row[4] = rs.getString("horaInicio");
                row[5] = rs.getString("duracion");
                row[6] = rs.getString("salon");
                row[7] = rs.getString("edificio");
                row[8] = rs.getString("escuela");
                row[9] = rs.getString("carrera");
                row[10] = rs.getString("semestre");
                data.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("ERROR");
        }
        return data;
    }

    public void deleteClass(String codigo){
        String deleteSQLQuery = "DELETE FROM Clases WHERE codigo = ?";
        try (Connection conn = getConection(); PreparedStatement pstmt = conn.prepareStatement(deleteSQLQuery)) {
            pstmt.setString(1, codigo);
            pstmt.executeUpdate();
        }  catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Fallo al eliminar clase");
        }

    }

    public void updateCapacidad(String codigo, int nuevaCapacidad){
        String sqlQuery = "UPDATE clases SET capacidad = ? WHERE codigo = ?";
        try (Connection conn = getConection(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, nuevaCapacidad);
            pstmt.setString(2, codigo);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Capacidad actualizada correctamente.");
            } else {
                System.out.println("No se pudo actualizar la capacidad.");
            }
        }  catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Fallo actualizar la capacidad de la clase (cap - 1)");
        }

    }

}

