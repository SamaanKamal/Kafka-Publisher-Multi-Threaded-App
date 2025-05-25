package org.example.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.example.Entity.User;
import org.example.Producer.NotificationProducer;
import org.example.Service.UserService;
import org.example.Util.JSONUtil;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserConsumer {
    private final GenericConsumer<String, String> consumer;
    private final NotificationProducer notificationProducer;
    private final UserService userService;

    private static final long PERIOD = 2 * 60 * 1000; // 2 minutes

    public UserConsumer() {
        this.consumer = new GenericConsumer<>("src/main/resources/kafka-consumer-config.properties");
        this.notificationProducer = new NotificationProducer();
        userService = new UserService();
    }

    public void startConsuming() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Polling for user messages...");

                consumer.consume(
                        Collections.singletonList("users"),
                        (ConsumerRecord<String, String> record) -> {
                            try {
                                User user = JSONUtil.fromJson(record.value(), User.class);
                                System.out.println("Consumed user: " + user.getName());
                                userService.handleUserCreation(user);

                                // Trigger notification
                                notificationProducer.createNotification(user);
                            } catch (Exception e) {
                                System.err.println("Failed to process user message: " + e.getMessage());
                                e.printStackTrace();
                            }
                        },
                        Duration.ofSeconds(10)
                );
            }
        };

        timer.scheduleAtFixedRate(task, 0, PERIOD);
        System.out.println("User consumer scheduled every 2 minutes.");
    }
}
