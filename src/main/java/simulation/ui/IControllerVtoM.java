package simulation.ui;
/**
 * Interface for View-to-Model communication in the simulation.
 * <p>
 * Defines methods that the UI (view) calls to control the simulation engine (model),
 * such as starting, pausing, or adjusting simulation speed.
 * </p>
 */
public interface IControllerVtoM {
    /**
     * Starts the simulation engine with the configured parameters.
     */
    void startSimulation();
    /**
     * Increases the speed of the simulation by reducing the delay between simulation steps.
     */

    void increaseSpeed();
    /**
     * Decreases the speed of the simulation by increasing the delay between simulation steps.
     */
    void decreaseSpeed();
    /**
     * Pauses the simulation engine.
     */
    void pauseSimulation();
    /**
     * Resumes the simulation engine after being paused.
     */
    void resumeSimulation();
    /**
     * Executes a single step of the simulation (used in step-by-step mode).
     */
    void stepSimulation();
    /**
     * Checks whether the simulation is currently running.
     *
     * @return true if the simulation engine is running, false otherwise
     */
    boolean isSimulationRunning();
    /**
     * Sets the delay (in milliseconds) between simulation steps.
     * This affects the simulation speed.
     *
     * @param delay the delay in milliseconds
     */
    void setDelay(long delay);
}
