package org.example.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.Producer.GenericProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class GenericConsumer<K, V> {
    private final KafkaConsumer<K, V> consumer;
    private static final Logger logger = LoggerFactory.getLogger(GenericConsumer.class);


    public GenericConsumer(String configFile) {
        Properties userProps = loadProperties(configFile);
        // Make sure the required configuration properties are loaded
        if (!userProps.containsKey("bootstrap.servers") ||
                !userProps.containsKey("key.deserializer") ||
                !userProps.containsKey("value.deserializer")) {
            logger.error("Missing required Kafka config properties.");
            throw new IllegalArgumentException("Missing required Kafka config properties.");
        }
        this.consumer = new KafkaConsumer<>(userProps);
    }

    public void consume(List<String> topics) {
        consumer.subscribe(topics);

        while (true) {
            ConsumerRecords<?, ?> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<?, ?> record : records) {
                System.out.printf("Key: %s, Value: %s, Topic: %s%n",
                        record.key(), record.value(), record.topic());
            }
        }
    }
    public void consume(List<String> topics, Consumer<ConsumerRecord<K, V>> recordHandler, Duration duration) {
        consumer.subscribe(topics);

        ConsumerRecords<K, V> records = consumer.poll(duration);
        for (ConsumerRecord<K, V> record : records) {
            recordHandler.accept(record);
        }
    }

    public void close() {
        consumer.close();
    }

    private static Properties loadProperties(String configFile) {
        System.out.println(configFile);
        Properties userProps = new Properties();
        try (FileInputStream in = new FileInputStream(configFile)) {
            userProps.load(in);
        } catch (IOException e) {
            logger.error("Failed to load Kafka producer config", e);
            throw new RuntimeException("Failed to load Kafka producer config", e);
        }
        return userProps;
    }
}
