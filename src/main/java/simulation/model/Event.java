package simulation.model;

/**
 * Represents a discrete event in the simulation.
 *
 * <p>Events are ordered by time and can be either ARRIVAL or DEPARTURE
 * events. Each event is associated with a customer and a target
 * service point.
 */

public class Event implements Comparable<Event> {
    public static final int ARRIVAL = 1;
    public static final int DEPARTURE = 2;

    private final double time;
    private final int type;
    private final Customer customer;
    private final ServicePoint target;

    public Event(double time, int type, Customer customer, ServicePoint target) {
        this.time = time;
        this.type = type;
        this.customer = customer;
        this.target = target;
    }

    public double getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ServicePoint getTarget() {
        return target;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    @Override
    public String toString() {
        String t = (type == ARRIVAL) ? "ARRIVAL" : "DEPARTURE";
        return String.format("[%.2f] %s of %s at %s", time, t, customer, target.getName());
    }
}
