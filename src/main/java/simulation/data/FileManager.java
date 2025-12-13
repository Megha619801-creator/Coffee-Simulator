package simulation.data;

import simulation.config.SimulationParameters;
import simulation.statistics.ServicePointStatistics;
import simulation.statistics.SimulationStatistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
/**
 * Utility class for reading simulation configuration and writing simulation results.
 * <p>
 * This class provides methods to:
 * <ul>
 *     <li>Load {@link SimulationParameters} from a properties file.</li>
 *     <li>Write {@link SimulationStatistics} to a CSV file in a structured format.</li>
 * </ul>
 * <p>
 * All methods are static; the class cannot be instantiated.
 * </p>
 */
public class FileManager {

    private FileManager() {
    }
    /**
     * Loads simulation parameters from a properties file.
     * <p>
     * Supported properties:
     * <ul>
     *     <li>instore.arrival.mean</li>
     *     <li>mobile.arrival.mean</li>
     *     <li>cashier.service.mean</li>
     *     <li>barista.service.mean</li>
     *     <li>barista.service.variance</li>
     *     <li>shelf.service.min</li>
     *     <li>shelf.service.max</li>
     *     <li>delivery.service.time</li>
     *     <li>simulation.duration</li>
     * </ul>
     *
     * @param path path to the properties file
     * @return a {@link SimulationParameters} object built from the file
     * @throws IOException if the file cannot be read
     */
    public static SimulationParameters loadParameters(Path path) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        }
        SimulationParameters.Builder builder = SimulationParameters.builder();
        if (props.containsKey("instore.arrival.mean")) {
            builder.instoreArrivalMean(Double.parseDouble(props.getProperty("instore.arrival.mean")));
        }
        if (props.containsKey("mobile.arrival.mean")) {
            builder.mobileArrivalMean(Double.parseDouble(props.getProperty("mobile.arrival.mean")));
        }
        if (props.containsKey("cashier.service.mean")) {
            builder.cashierServiceMean(Double.parseDouble(props.getProperty("cashier.service.mean")));
        }
        if (props.containsKey("barista.service.mean")) {
            builder.baristaServiceMean(Double.parseDouble(props.getProperty("barista.service.mean")));
        }
        if (props.containsKey("barista.service.variance")) {
            builder.baristaServiceVariance(Double.parseDouble(props.getProperty("barista.service.variance")));
        }
        if (props.containsKey("shelf.service.min")) {
            builder.shelfServiceMin(Double.parseDouble(props.getProperty("shelf.service.min")));
        }
        if (props.containsKey("shelf.service.max")) {
            builder.shelfServiceMax(Double.parseDouble(props.getProperty("shelf.service.max")));
        }
        if (props.containsKey("delivery.service.time")) {
            builder.deliveryServiceTime(Double.parseDouble(props.getProperty("delivery.service.time")));
        }
        if (props.containsKey("simulation.duration")) {
            builder.simulationDuration(Double.parseDouble(props.getProperty("simulation.duration")));
        }
        return builder.build();
    }
    /**
     * Writes simulation statistics to a CSV file.
     * <p>
     * The output contains two sections:
     * <ul>
     *     <li>System summary metrics (simulation time, arrivals, departures, averages, etc.)</li>
     *     <li>Per-service-point statistics (arrivals, completions, utilization, throughput, average service time)</li>
     * </ul>
     *
     * @param outputFile path to the CSV output file
     * @param statistics simulation statistics to write
     * @throws IOException if the file cannot be written
     * @throws IllegalArgumentException if statistics is null
     */
    public static void writeStatistics(Path outputFile, SimulationStatistics statistics) throws IOException {
        if (statistics == null) {
            throw new IllegalArgumentException("statistics must not be null");
        }
        Path parent = outputFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write("# Simulation Summary");
            writer.newLine();
            writer.write("Metric;Value");
            writer.newLine();
            writeSummaryLine(writer, "Simulation time", statistics.getSimulationTime());
            writeSummaryLine(writer, "Total arrivals", statistics.getTotalArrivals());
            writeSummaryLine(writer, "Total departures", statistics.getTotalDepartures());
            writeSummaryLine(writer, "Average waiting time", statistics.getAverageWaitingTime());
            writeSummaryLine(writer, "Average response time", statistics.getAverageResponseTime());
            writeSummaryLine(writer, "Average service time", statistics.getAverageServiceTimePerCustomer());
            writeSummaryLine(writer, "Throughput", statistics.getThroughput());
            writeSummaryLine(writer, "Average number in system", statistics.getAverageNumberInSystem());
            writer.newLine();
            writer.write("# Service Point Statistics");
            writer.newLine();
            writer.write("Name;Arrivals;Completions;Utilization;Throughput;AvgServiceTime");
            writer.newLine();
            for (ServicePointStatistics stats : statistics.getServicePointStatistics()) {
                writer.write(String.format(Locale.US, "%s;%d;%d;%.3f;%.3f;%.3f",
                        stats.getServicePointName(),
                        stats.getArrivals(),
                        stats.getCompletions(),
                        stats.getUtilization(statistics.getSimulationTime()),
                        stats.getThroughput(statistics.getSimulationTime()),
                        stats.getAverageServiceTime()));
                writer.newLine();
            }
        }
    }

    private static void writeSummaryLine(BufferedWriter writer, String label, double value) throws IOException {
        writer.write(String.format(Locale.US, "%s;%.3f", label, value));
        writer.newLine();
    }

    private static void writeSummaryLine(BufferedWriter writer, String label, int value) throws IOException {
        writer.write(String.format(Locale.US, "%s;%d", label, value));
        writer.newLine();
    }
}
