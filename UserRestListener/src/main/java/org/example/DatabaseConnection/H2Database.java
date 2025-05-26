package org.example.DatabaseConnection;

import java.sql.*;

public class H2Database {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb"; // or use file-based if needed
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    public static Connection connection;

    static {
        try {
            // Load driver (optional for JDBC 4+)
            Class.forName("org.h2.Driver");

            // Create connection
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE users(id INT PRIMARY KEY, name VARCHAR(255),  age int)");
            stmt.execute("CREATE TABLE notifications(id INT PRIMARY KEY, user_id int,  message VARCHAR(255), timestamp timestamp)");
            stmt.execute("INSERT INTO users VALUES(1, 'Alice', 45), (2, 'Bob', 30)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static PreparedStatement prepare(String sql) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not available.");
        }
        return connection.prepareStatement(sql);
    }
}
