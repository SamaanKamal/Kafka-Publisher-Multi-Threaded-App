package org.example.Producer;

import org.example.Entity.Notification;
import org.example.Entity.User;
import org.example.Util.JSONUtil;

import java.sql.Timestamp;

public class NotificationProducer {
    private final GenericProducer<String, String> producer;

    public NotificationProducer() {
        this.producer = new GenericProducer<>("src/main/resources/kafka-producer-config.properties");

    }

    public void createNotification(User user){
        // Create notification
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setMessage("User created: " + user.getName());
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));

        // Publish notification to Kafka
        String notificationJson = JSONUtil.toJson(notification);
        producer.send("notifications", String.valueOf(notification.getUserId()), notificationJson);

        System.out.println("User and notification handled successfully.");
    }
}
