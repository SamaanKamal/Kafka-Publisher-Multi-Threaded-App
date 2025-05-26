package org.example.Service;

import org.example.Consumer.NotificationConsumer;
import org.example.DatabaseConnection.DatabaseCRUD;
import org.example.Entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {
    private final DatabaseCRUD db;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService() {
        this.db = new DatabaseCRUD();
    }

    public int handleNotificationSaving(Notification notification) {
        logger.info("dog in notification service");
        // Save user to database
        return db.saveNotification(notification);
    }


}
