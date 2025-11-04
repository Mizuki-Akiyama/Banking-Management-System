package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/Banking_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "jyk7890./";

    // Method to establish a connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver Not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

    // Test connection
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✅ Connected to Database Successfully!");
        } else {
            System.out.println("❌ Failed to connect to Database!");
        }
    }
}
