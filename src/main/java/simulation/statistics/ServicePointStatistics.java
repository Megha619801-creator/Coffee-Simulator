package simulation.statistics;

/**
 * Immutable statistics snapshot for a single service point in the simulation.
 * <p>
 * Tracks the number of customers that arrived and completed service at this service point,
 * the total service time, and provides derived metrics such as utilization, throughput,
 * and average service time.
 * </p>
 */
public final class ServicePointStatistics {

    private final String servicePointName;
    private final int arrivals;
    private final int completions;
    private final double totalServiceTime;
    /**
     * Constructs a snapshot of statistics for a service point.
     *
     * @param servicePointName the name of the service point
     * @param arrivals         total number of customers that arrived
     * @param completions      total number of customers that completed service
     * @param totalServiceTime total cumulative service time for all completed customers
     */
    public ServicePointStatistics(String servicePointName,
            int arrivals,
            int completions,
            double totalServiceTime) {
        this.servicePointName = servicePointName;
        this.arrivals = arrivals;
        this.completions = completions;
        this.totalServiceTime = totalServiceTime;
    }
    /**
     * Returns the name of the service point.
     *
     * @return service point name
     */
    public String getServicePointName() {
        return servicePointName;
    }
    /**
     * Returns the total number of arrivals at this service point.
     *
     * @return number of arrivals
     */
    public int getArrivals() {
        return arrivals;
    }
    /**
     * Returns the total number of service completions at this service point.
     *
     * @return number of completed services
     */
    public int getCompletions() {
        return completions;
    }
    /**
     * Returns the total cumulative service time for all completed customers.
     *
     * @return total service time
     */
    public double getTotalServiceTime() {
        return totalServiceTime;
    }
    /**
     * Calculates the utilization of this service point.
     * <p>
     * Utilization is defined as the proportion of simulation time spent serving customers.
     * Returns 0.0 if the simulation time is non-positive.
     * </p>
     *
     * @param simulationTime total simulation time
     * @return utilization as a fraction between 0.0 and 1.0
     */
    public double getUtilization(double simulationTime) {
        if (simulationTime <= 0) {
            return 0.0;
        }
        return totalServiceTime / simulationTime;
    }
    /**
     * Calculates the throughput of this service point.
     * <p>
     * Throughput is the number of completed customers divided by the simulation time.
     * Returns 0.0 if the simulation time is non-positive.
     * </p>
     *
     * @param simulationTime total simulation time
     * @return throughput (customers per unit time)
     */
    public double getThroughput(double simulationTime) {
        if (simulationTime <= 0) {
            return 0.0;
        }
        return completions / simulationTime;
    }
    /**
     * Returns the average service time per completed customer.
     * Returns 0.0 if no customers have completed service.
     *
     * @return average service time
     */
    public double getAverageServiceTime() {
        if (completions == 0) {
            return 0.0;
        }
        return totalServiceTime / completions;
    }
}
