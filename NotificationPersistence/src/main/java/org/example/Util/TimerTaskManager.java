package org.example.Util;

import org.example.Config.AppLifeCycleListener;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerTaskManager implements AppLifeCycleListener {
    private Timer timer;

    @Override
    public void onAppStart() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Custom listener task running...");
            }
        }, 0, 1000* 60 * 2);
    }

    @Override
    public void onAppShutdown() {
        timer.cancel();
        System.out.println("Timer stopped.");
    }
}
