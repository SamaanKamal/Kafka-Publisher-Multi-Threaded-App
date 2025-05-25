package org.example;

import org.example.Consumer.UserConsumer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserConsumer consumer = new UserConsumer();
        consumer.startConsuming();
    }
}