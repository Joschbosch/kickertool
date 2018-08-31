package zur.koeln.kickertool.tools;


public interface Timer {

    public void setTimer(int minutesPerMatch);

    void start();

    void stop();

    void reset();

    long getMinutesLeft();

    long getSecondsLeft();

    long getMilliSecondsLeft();

    public boolean isRunning();
}
