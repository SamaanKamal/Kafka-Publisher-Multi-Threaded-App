package org.example.Producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GenericProducer<K,V> {
    private static final Logger logger = LoggerFactory.getLogger(GenericProducer.class);
    private final KafkaProducer<K, V> producer;


    public GenericProducer(String configFile) {
        Properties userProps = loadProperties(configFile);
        // Make sure the required configuration properties are loaded
        if (!userProps.containsKey("bootstrap.servers") ||
                !userProps.containsKey("key.serializer") ||
                !userProps.containsKey("value.serializer")) {
            logger.error("Missing required Kafka config properties.");
            throw new IllegalArgumentException("Missing required Kafka config properties.");
        }
        this.producer = new KafkaProducer<>(userProps);
    }


    public void send(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                logger.error("❌ Kafka send failed:", exception.getMessage());
            } else {
                logger.info("Notification sent to Kafka: {} partition {} offset {}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    public void close() {
        try {
            producer.close();
        } catch (Exception e) {
            logger.error("Error while closing the producer", e);
        }
    }

    private static Properties loadProperties(String configFile) {
        System.out.println(configFile);
        Properties userProps = new Properties();
        try (InputStream in = GenericProducer.class.getClassLoader().getResourceAsStream(configFile)) {
            if (in == null) {
                throw new FileNotFoundException("Config file not found in classpath: " + configFile);
            }
            userProps.load(in);
        } catch (IOException e) {
            logger.error("Failed to load Kafka producer config", e);
            throw new RuntimeException("Failed to load Kafka producer config", e);
        }
        return userProps;
    }
}
