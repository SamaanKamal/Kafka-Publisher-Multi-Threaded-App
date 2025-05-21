package org.example.Generic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class GenericConsumer<K, V> {
    private final KafkaConsumer<?, ?> consumer;

    public GenericConsumer() {
        Properties userProps = new Properties();
        try (FileInputStream in = new FileInputStream("kafka-config.properties")) {
            userProps.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Kafka consumer config", e);
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

    public void close() {
        consumer.close();
    }
}
