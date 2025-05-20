package org.example.Util;

import java.util.TimerTask;

public class Timer {
    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("Running timer task at: " + java.time.LocalTime.now());
            }
        };

        long delay = 2000L;  // initial delay
        long period = 5000L; // repeat every 5 seconds

        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }


}
