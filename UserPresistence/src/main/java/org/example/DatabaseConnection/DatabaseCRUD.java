package org.example.DatabaseConnection;

import org.example.Entity.Notification;
import org.example.Entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.example.DatabaseConnection.H2Database.connection;

public class DatabaseCRUD {
    public void save(User user) {
        String sql = "MERGE INTO users (id, name, age) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = H2Database.prepare(sql)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setInt(3, user.getAge());
            stmt.executeUpdate();
            System.out.println("inserted id: "+ user.getId() + ", name: " + user.getName() + ", age: " + user.getAge());
        } catch (SQLException e) {
            System.err.println("Failed to save user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to close DB connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = H2Database.prepare(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Failed to find user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = H2Database.prepare(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    // UPDATE (just an example to update name and age by ID)
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement stmt = H2Database.prepare(sql)) {
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to update user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = H2Database.prepare(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to delete user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveNotification(Notification n) {
        String sql = "INSERT INTO notifications (user_id, message, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement ps = H2Database.prepare(sql)) {
            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getMessage());
            ps.setTimestamp(3, n.getTimestamp());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
