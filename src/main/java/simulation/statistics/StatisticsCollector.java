package simulation.statistics;

import simulation.logic.SimulationListener;
import simulation.model.Customer;
import simulation.model.Event;
import simulation.model.ServicePoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Collects statistics for both individual service points and the overall system
 * during a simulation run.
 * <p>
 * Tracks per-service-point metrics such as arrivals, departures, and total service time,
 * and system-level metrics such as total arrivals, departures, waiting time, service time,
 * and response time.
 * </p>
 * <p>
 * This class implements {@link SimulationListener} and should be registered with the simulation engine
 * to automatically update statistics when events occur.
 * </p>
 */
public class StatisticsCollector implements SimulationListener {

    private final Map<ServicePoint, MutableStats> perServicePoint = new LinkedHashMap<>();
    private final Set<ServicePoint> terminalServicePoints = new HashSet<>();
    private final Map<Customer, Double> customerServiceTimes = new HashMap<>();

    private int systemArrivals;
    private int systemDepartures;
    private double totalSystemServiceTime;
    private double totalSystemWaitTime;
    private double totalSystemResponseTime;
    /**
     * Registers a service point for statistics collection.
     *
     * @param servicePoint the service point to track
     * @param terminal     whether this service point is a terminal/exit point for customers
     */
    public void registerServicePoint(ServicePoint servicePoint, boolean terminal) {
        perServicePoint.computeIfAbsent(servicePoint, sp -> new MutableStats(servicePoint.getName()));
        if (terminal) {
            terminalServicePoints.add(servicePoint);
        }
    }
    /**
     * Resets all collected statistics.
     */
    public void reset() {
        systemArrivals = 0;
        systemDepartures = 0;
        totalSystemServiceTime = 0.0;
        totalSystemWaitTime = 0.0;
        totalSystemResponseTime = 0.0;
        customerServiceTimes.clear();
        perServicePoint.values().forEach(MutableStats::reset);
    }
    /**
     * Called when a customer arrives at a service point.
     *
     * @param event the arrival event
     */
    @Override
    public void onArrival(Event event) {
        perServicePoint.computeIfAbsent(event.getTarget(),
                sp -> new MutableStats(sp.getName())).recordArrival();
        customerServiceTimes.putIfAbsent(event.getCustomer(), 0.0);
        systemArrivals++;
    }
    /**
     * Called when a customer is routed from one service point to another.
     *
     * @param customer the customer being routed
     * @param from     the originating service point
     * @param to       the target service point (may be null)
     */
    @Override
    public void onRouting(Customer customer, ServicePoint from, ServicePoint to) {
        if (to != null) {
            perServicePoint.computeIfAbsent(to,
                    sp -> new MutableStats(sp.getName())).recordArrival();
        }
    }
    /**
     * Called when a customer departs from a service point.
     * Updates both per-service-point and system-wide statistics.
     *
     * @param event       the departure event
     * @param waitTime    time spent waiting before service
     * @param serviceTime time spent in service
     */
    @Override
    public void onDeparture(Event event, double waitTime, double serviceTime) {
        ServicePoint target = event.getTarget();
        MutableStats stats = perServicePoint.computeIfAbsent(target,
                sp -> new MutableStats(sp.getName()));
        stats.recordDeparture(serviceTime);

        customerServiceTimes.merge(event.getCustomer(), serviceTime, Double::sum);

        if (terminalServicePoints.contains(target)) {
            double totalService = customerServiceTimes.getOrDefault(event.getCustomer(), serviceTime);
            double response = event.getCustomer().getResponseTime();
            double wait = Math.max(0.0, response - totalService);

            totalSystemServiceTime += totalService;
            totalSystemResponseTime += response;
            totalSystemWaitTime += wait;
            systemDepartures++;
            customerServiceTimes.remove(event.getCustomer());
        }
    }
    /**
     * Returns an immutable snapshot of the current simulation statistics.
     *
     * @param simulationTime the total simulation time
     * @return a {@link SimulationStatistics} object containing the current metrics
     */
    public SimulationStatistics snapshot(double simulationTime) {
        return new SimulationStatistics(
                simulationTime,
                perServicePoint.values()
                        .stream()
                        .map(MutableStats::toImmutable)
                        .collect(Collectors.toUnmodifiableList()),
                systemArrivals,
                systemDepartures,
                totalSystemServiceTime,
                totalSystemWaitTime,
                totalSystemResponseTime);
    }

    private static final class MutableStats {
        private final String name;
        private int arrivals;
        private int completions;
        private double totalServiceTime;

        private MutableStats(String name) {
            this.name = name;
        }

        private void recordArrival() {
            arrivals++;
        }

        private void recordDeparture(double serviceTime) {
            completions++;
            totalServiceTime += serviceTime;
        }

        private void reset() {
            arrivals = 0;
            completions = 0;
            totalServiceTime = 0.0;
        }

        private ServicePointStatistics toImmutable() {
            return new ServicePointStatistics(name, arrivals, completions, totalServiceTime);
        }
    }
}
