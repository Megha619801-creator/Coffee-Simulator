package simulation.ui;

import java.text.DecimalFormat;
/**
 * Immutable summary of a single simulation run.
 * <p>
 * Contains key statistics such as total simulation time, total customers served,
 * and average waiting time.
 * </p>
 */
public record SimulationSummary(double simulationTime,
        int totalCustomersServed,
        double averageWaitingTime) {

    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#0.00");
    private static final DecimalFormat SUMMARY_FORMAT = new DecimalFormat("#0.0");


    /**
     * Returns the average waiting time formatted as a string with one decimal place.
     *
     * @return formatted average waiting time
     */
    public String formattedSimulationTime() {
        return TIME_FORMAT.format(simulationTime);
    }
    /**
     * Returns the average waiting time formatted as a string with one decimal place.
     *
     * @return formatted average waiting time
     */
    public String formattedAverageWaitingTime() {
        return SUMMARY_FORMAT.format(averageWaitingTime);
    }
    /**
     * Returns a concise textual summary of the simulation run.
     * Example: "15 customers, avg wait 2.3 min"
     *
     * @return summary line as string
     */
    public String toSummaryLine() {
        return totalCustomersServed + " customers, avg wait "
                + formattedAverageWaitingTime() + " min";
    }
}
