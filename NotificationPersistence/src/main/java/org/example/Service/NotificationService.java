package org.example.Service;

import org.example.DatabaseConnection.DatabaseCRUD;
import org.example.Entity.Notification;

public class NotificationService {
    private final DatabaseCRUD db;
    public NotificationService() {
        this.db = new DatabaseCRUD();
    }

    public int handleNotificationSaving(Notification notification) {
        // Save user to database
        return db.saveNotification(notification);
    }


}
