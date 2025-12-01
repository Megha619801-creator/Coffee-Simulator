package simulation.ui;

import simu.model.Customer;

public interface IControllerMtoV {
    void showEndTime(double time);

    void visualiseCustomer(Customer c, int servicePointIndex);

    void removeCustomer(Customer c);
}
