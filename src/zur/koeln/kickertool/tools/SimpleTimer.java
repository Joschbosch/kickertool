package zur.koeln.kickertool.tools;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleTimer
    implements Timer {

    private int minutesPerMatch = -1;
    private final Thread timerThread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong currentTime;

    public SimpleTimer() {
        currentTime = new AtomicLong(0);

        timerThread = new Thread() {
            @Override
            public void run() {
                
                long lastTime = System.currentTimeMillis();
                while (running.get()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                    long now = System.currentTimeMillis();
                    long diff = now - lastTime;
                    currentTime.set(currentTime.get() - diff);
                    lastTime = now;
                    if (currentTime.get() <= 0) {
                        running.set(false);
                        currentTime.set(0);
                    }
                }
            }
        };

    }

    public void setTimer(int minutesPerMatch) {
        this.minutesPerMatch = minutesPerMatch;
        reset();
    }
    @Override
    public long getMinutesLeft() {
        return (currentTime.get() / 1000) / 60;
    }
    @Override
    public long getSecondsLeft() {
        return (currentTime.get() / 1000) % 60;
    }

    @Override
    public long getMilliSecondsLeft() {
        return (currentTime.get() % 1000);
    }

    public void start() {
        if (running.get()) {
            return;
        }
        if (minutesPerMatch == -1) {
            //Error
        }
        running.set(true);
        timerThread.start();
    }

    public void stop() {
        running.set(false);
    }

    public void reset() {
        if (!running.get()) {
            currentTime.set(minutesPerMatch * 60L * 1000L);
        }
    }
    @Override
    public boolean isRunning() {
        return running.get();
    }
}
