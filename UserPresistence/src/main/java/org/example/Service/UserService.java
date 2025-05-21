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
}
