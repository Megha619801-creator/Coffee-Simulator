package simulation.model;
/**
 * Represents a customer in the cafe simulation.
 * <p>
 * Each customer has:
 * <ul>
 *   <li>A unique ID</li>
 *   <li>A type (INSTORE or MOBILE)</li>
 *   <li>Arrival, service, and departure times</li>
 * </ul>
 *
 * Customer objects are immutable in identity but mutable in
 * timing information during simulation.
 */

public class Customer {
    private static int counter = 0;
    private final int id;
    private final String type; // "INSTORE" or "MOBILE"
    private double arrivalTime;
    private double serviceStartTime;
    private double serviceEndTime;
    private double totalServiceDuration;

    /**
     * Creates a new customer.
     *
     * @param type customer type
     * @param arrivalTime arrival time into the system
     */
    public Customer(String type, double arrivalTime) {
        this.id = ++counter;
        this.type = type;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setServiceStartTime(double time) {
        this.serviceStartTime = time;
    }

    public void setServiceEndTime(double time) {
        this.serviceEndTime = time;
    }

    public double getServiceTime() {
        if (Double.isNaN(serviceStartTime) || Double.isNaN(serviceEndTime)) {
            return 0.0;
        }
        return serviceEndTime - serviceStartTime;
    }

    public double getWaitingTime() {
        if (Double.isNaN(serviceStartTime)) {
            return 0.0;
        }
        return serviceStartTime - arrivalTime;
    }

    public double getResponseTime() {
        if (Double.isNaN(serviceEndTime)) {
            return 0.0;
        }
        return serviceEndTime - arrivalTime;
    }

    public void addServiceDuration(double duration) {
        if (duration > 0) {
            totalServiceDuration += duration;
        }
    }

    public double getTotalServiceDuration() {
        return totalServiceDuration;
    }

    public double getTotalWaitingTime() {
        double response = getResponseTime();
        if (response <= 0) {
            return 0.0;
        }
        return Math.max(0.0, response - totalServiceDuration);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
