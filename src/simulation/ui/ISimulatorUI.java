package simulation.ui;

public interface ISimulatorUI {
    double getTime();

    long getDelay();

    void setEndingTime(double time);

    IVisualisation getVisualisation();
}
