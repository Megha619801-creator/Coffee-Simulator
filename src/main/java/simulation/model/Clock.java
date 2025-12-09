package simulation.model;

/**
 * Global simulation clock implemented as a Singleton.
 * Provides centralized time control for all simulator components.
 */
public final class Clock {

    private static final Clock INSTANCE = new Clock();
    private double currentTime = 0.0;

    private Clock() {
    }

    public static Clock getInstance() {
        return INSTANCE;
    }

    public double getTime() {
        return currentTime;
    }

    public void setTime(double time) {
        if (time < 0) {
            throw new IllegalArgumentException("Clock time cannot be negative.");
        }
        currentTime = time;
    }

    public void advance(double delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Clock delta cannot be negative.");
        }
        currentTime += delta;
    }

    public void reset() {
        currentTime = 0.0;
    }
}
