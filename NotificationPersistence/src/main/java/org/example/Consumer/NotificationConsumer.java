package org.example.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Entity.Notification;
import org.example.Service.NotificationService;
import org.example.Util.JSONUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationConsumer {
    private final GenericConsumer<String, String> consumer;
    private final NotificationService notificationService;

    private static final long PERIOD = 2 * 60 * 1000; // 2 minutes

    public NotificationConsumer() {
        this.consumer = new GenericConsumer<>("src/main/resources/kafka-consumer-config.properties");
        notificationService = new NotificationService();
    }

    public void startConsuming() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Polling for notification messages...");

                consumer.consume(
                        Collections.singletonList("notifications"),
                        (ConsumerRecord<String, String> record) -> {
                            try {
                                Notification notification = JSONUtil.fromJson(record.value(), Notification.class);
                                int notificationId = notificationService.handleNotificationSaving(notification);
                                System.out.println("Consumed notification with id : " + notificationId + " for user: " + notification.getUserId() + " with message: " + notification.getMessage());


                                // Trigger notification
                            } catch (Exception e) {
                                System.err.println("Failed to process notification message: " + e.getMessage());
                                e.printStackTrace();
                            }
                        },
                        Duration.ofSeconds(10)
                );
            }
        };

        timer.scheduleAtFixedRate(task, 0, PERIOD);
        System.out.println("notification consumer scheduled every 2 minutes.");
    }
}
