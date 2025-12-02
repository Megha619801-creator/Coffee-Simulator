package simulation.ui;

import java.text.DecimalFormat;

public record SimulationSummary(double simulationTime,
        int totalCustomersServed,
        double averageWaitingTime) {

    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#0.00");
    private static final DecimalFormat SUMMARY_FORMAT = new DecimalFormat("#0.0");

    public String formattedSimulationTime() {
        return TIME_FORMAT.format(simulationTime);
    }

    public String formattedAverageWaitingTime() {
        return SUMMARY_FORMAT.format(averageWaitingTime);
    }

    public String toSummaryLine() {
        return totalCustomersServed + " customers, avg wait "
                + formattedAverageWaitingTime() + " min";
    }
}
