package org.example.Service;

import org.example.DatabaseConnection.DatabaseCRUD;
import org.example.Entity.User;
import org.example.Producer.GenericProducer;
import org.example.Producer.NotificationProducer;
import org.example.Util.JSONUtil;

public class UserService {
    private final DatabaseCRUD db;

    public UserService() {
        this.db = new DatabaseCRUD();
    }

    public void handleUserCreation(User user) {
        // Save user to database
        db.save(user);

    }
}
