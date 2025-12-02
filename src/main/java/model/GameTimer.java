package model;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private Timer timer = new Timer();
    private int secondsElapsed = 0;

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
            }
        }, 1000, 1000);
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public void setSecondsElapsed(int seconds) {
        this.secondsElapsed = seconds;
    }

    public void stop() {
        timer.cancel();
    }

    public void reset() {
        secondsElapsed = 0;
    }   
}
