package simulation.model;

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
