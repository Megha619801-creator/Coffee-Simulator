package simulation.ui;

import simulation.model.Customer;

/**
 * Interface for Model-to-View communication in the simulation.
 * <p>
 * Defines methods that the simulation engine (model) calls
 * to update the UI (view) about simulation events.
 * </p>
 */
public interface IControllerMtoV {
    /**
     * Informs the view that the simulation has ended and provides
     * the final simulation time.
     *
     * @param time the time at which the simulation ended
     */
    void showEndTime(double time);
    /**
     * Requests the view to visualize a customer at a specific service point.
     *
     * @param c the customer to be visualized
     * @param servicePointIndex the index of the service point in the UI
     */
    void visualiseCustomer(Customer c, int servicePointIndex);
    /**
     * Requests the view to remove a customer from the visualization
     * and update statistics if necessary.
     *
     * @param c the customer to remove
     */
    void removeCustomer(Customer c);
}
