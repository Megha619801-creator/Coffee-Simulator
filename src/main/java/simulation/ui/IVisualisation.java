package simulation.ui;

import simulation.model.Customer;
/**
 * Interface representing the visualisation component of the simulator.
 * <p>
 * Provides methods for displaying and managing customers visually in
 * the simulation, including adding, moving, and removing customers.
 * </p>
 */
public interface IVisualisation {
    void clearDisplay();

    void addCustomer(Customer customer, int servicePointIndex);

    void moveCustomer(Customer customer, int servicePointIndex);

    void removeCustomer(Customer customer);
}
