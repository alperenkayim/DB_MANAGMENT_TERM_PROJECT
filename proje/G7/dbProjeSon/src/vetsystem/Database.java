package vetsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/VetDB4";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "123";
    private static Connection conn = null;

    public static void connect() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Bağlantı tamam");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Bağlantı hatası");
        }
    }
    
    public static Connection getConnection() {
        return conn;
    }

    public static ResultSet find(String query, String... parameters) {
        ResultSet rs = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
            }
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    public static int update(String query, String... parameters) {
        int rowsAffected = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
            }

            rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Bağlantı kapandı");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
