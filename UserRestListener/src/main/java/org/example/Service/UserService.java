package org.example.Service;

import org.example.DatabaseConnection.DatabaseCRUD;
import org.example.Entity.Notification;
import org.example.Entity.User;
import org.example.Producer.GenericProducer;
import org.example.Producer.NotificationProducer;
import org.example.Util.JSONUtil;

import java.sql.Timestamp;

public class UserService {
    private final DatabaseCRUD db;
    private final NotificationProducer notificationProducer;

    public UserService() {
        this.db = new DatabaseCRUD();
        this.notificationProducer = new NotificationProducer();
    }

    public void handleUserCreation(User user) {
        // Save user to database
        db.save(user);

        notificationProducer.createNotification(user);
    }

    public void sendUserToKafka(User user) {
        GenericProducer<String, String> producer = new GenericProducer<>("kafka-config.properties");
        try {
            String json = JSONUtil.toJson(user);
            producer.send("users", String.valueOf(user.getId()), json);
        } catch (Exception e) {
            System.err.println("❌ Failed to send user to Kafka from HTTP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
