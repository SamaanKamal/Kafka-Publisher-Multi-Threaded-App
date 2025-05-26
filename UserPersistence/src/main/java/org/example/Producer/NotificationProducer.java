package org.example.Producer;

import org.example.Entity.Notification;
import org.example.Entity.User;
import org.example.Util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationProducer {
    private final GenericProducer<String, String> producer;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);
    private static final AtomicInteger idGenerator = new AtomicInteger(1); // Starting ID


    public NotificationProducer() {
        this.producer = new GenericProducer<>("kafka-producer-config.properties");

    }

    public void createNotification(User user){
        // Create notification
        Notification notification = new Notification();
        notification.setId(idGenerator.getAndIncrement());
        notification.setUserId(user.getId());
        notification.setMessage("User created: " + user.getName());
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));

        // Publish notification to Kafka
        String notificationJson = JSONUtil.toJson(notification);
        producer.send("notifications", String.valueOf(notification.getUserId()), notificationJson);

        System.out.println("User and notification handled successfully.");
        logger.info("User and notification handled successfully.");
    }
}
