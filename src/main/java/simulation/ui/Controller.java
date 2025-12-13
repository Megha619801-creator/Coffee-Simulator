package simulation.ui;

import javafx.application.Platform;
import simu.framework.Clock;
import simu.framework.IEngine;
import simulation.model.Customer;
import simu.model.MyEngine;

/**
 * Controller class for the simulation UI, implementing communication
 * between the view (UI) and the model (simulation engine).
 * <p>
 * Acts as both a view-to-model (VtoM) and model-to-view (MtoV) controller.
 * Responsible for starting, pausing, resuming, stepping the simulation,
 * visualizing customers, and summarizing simulation results.
 * </p>
 */
public class Controller implements IControllerVtoM, IControllerMtoV {
    private IEngine engine;
    private final ISimulatorUI ui;
    private int totalCustomersServed;
    private double cumulativeWaitingTime;

    /**
     * Constructs a Controller for a given simulation UI.
     *
     * @param ui the UI interface to be controlled
     */
    public Controller(ISimulatorUI ui) {
        this.ui = ui;
    }
    /**
     * Starts the simulation. Initializes the engine and simulation parameters.
     * If a simulation is already running, this method does nothing.
     */
    @Override
    public void startSimulation() {
        if (engine != null) {
            return;
        }
        Clock.getInstance().setTime(0); // ensure every run starts from time zero
        engine = new MyEngine(this);
        engine.setSimulationTime(ui.getTime());
        engine.setDelay(ui.getDelay());
        ui.showCurrentDelay(engine.getDelay());
        ui.getVisualisation().clearDisplay();
        totalCustomersServed = 0;
        cumulativeWaitingTime = 0.0;
        ((Thread) engine).start();
    }

    @Override
    public void decreaseSpeed() {
        if (engine == null) {
            return;
        }
        long updatedDelay = Math.max(1, (long) (engine.getDelay() * 1.10));
        engine.setDelay(updatedDelay);
        ui.showCurrentDelay(updatedDelay);
    }

    @Override
    public void increaseSpeed() {
        if (engine == null) {
            return;
        }
        long updatedDelay = Math.max(1, (long) (engine.getDelay() * 0.9));
        engine.setDelay(updatedDelay);
        ui.showCurrentDelay(updatedDelay);
    }

    @Override
    public void showEndTime(double time) {
        double averageWaiting = totalCustomersServed == 0 ? 0.0 : cumulativeWaitingTime / totalCustomersServed;
        SimulationSummary summary = new SimulationSummary(time, totalCustomersServed, averageWaiting);
        Platform.runLater(() -> ui.showSummary(summary));
        engine = null;
    }

    @Override
    public void visualiseCustomer(Customer c, int servicePointIndex) {
        Platform.runLater(() -> ui.getVisualisation().moveCustomer(c, servicePointIndex));
    }

    @Override
    public void removeCustomer(Customer c) {
        totalCustomersServed++;
        cumulativeWaitingTime += c.getTotalWaitingTime();
        Platform.runLater(() -> ui.getVisualisation().removeCustomer(c));
    }

    @Override
    public void pauseSimulation() {
        if (engine != null) {
            engine.pauseSimulation();
        }
    }

    @Override
    public void resumeSimulation() {
        if (engine != null) {
            engine.resumeSimulation();
        }
    }

    @Override
    public void stepSimulation() {
        if (engine != null) {
            engine.stepOnce();
        }
    }

    @Override
    public boolean isSimulationRunning() {
        return engine != null;
    }

    @Override
    public void setDelay(long delay) {
        if (engine == null) {
            return;
        }
        engine.setDelay(Math.max(1, delay));
        ui.showCurrentDelay(engine.getDelay());
    }
}
