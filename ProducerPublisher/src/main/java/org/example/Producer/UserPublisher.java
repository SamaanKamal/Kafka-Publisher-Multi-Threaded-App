package org.example.Producer;

import org.example.DatabaseConnection.DatabaseCRUD;
import org.example.Entity.User;
import org.example.Util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class UserPublisher {
    private static final Logger logger = LoggerFactory.getLogger(UserPublisher.class);
    private static final long DELAY = 0; // No initial delay
    private static final long PERIOD = 2 * 60 * 1000; // 2 minutes in milliseconds
    private final GenericProducer<String, String> producer;

    private static final AtomicInteger idGenerator = new AtomicInteger(100); // Starting ID

    public UserPublisher() {
        this.producer = new GenericProducer<>("kafka-config.properties");
    }

    public void start() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                User user = new User();
                user.setId(idGenerator.getAndIncrement());
                user.setName("User_" + user.getId());
                user.setAge((int) (Math.random() * 60 + 18)); // Random age between 18–78

                // Save to H2 database
//                DatabaseCRUD crud = new DatabaseCRUD();
//                crud.save(user);

                // Send to Kafka
                try  {
                    String json = JSONUtil.toJson(user);
                    producer.send("users", String.valueOf(user.getId()), json);
                } catch (Exception e) {
                    System.err.println("Kafka publish failed: " + e.getMessage());
                    e.printStackTrace();
                }

                System.out.println("Published user: " + user.getName());
                logger.info("Published user: " + user.getName());
            }
        };

        timer.scheduleAtFixedRate(task, DELAY, PERIOD);

        System.out.println("User publishing scheduled every 2 minutes.");
    }
}
