package simulation.ui;
/**
 * Interface representing the Simulator User Interface (UI).
 * <p>
 * Provides methods for the simulation controller to interact with the UI,
 * including retrieving user-configured simulation parameters, displaying
 * simulation progress, and showing the simulation summary.
 * </p>
 */
public interface ISimulatorUI {
    double getTime();

    long getDelay();

    void setEndingTime(double time);

    IVisualisation getVisualisation();

    void showCurrentDelay(long delay);

    void showSummary(SimulationSummary summary);
}
