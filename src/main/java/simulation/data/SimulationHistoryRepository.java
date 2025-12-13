package simulation.data;

import simulation.ui.SimulationSummary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
/**
 * Repository for storing and retrieving a history of simulation runs.
 * <p>
 * This class maintains a CSV file ("output/simulation-history.csv") containing
 * records of previous simulation runs. Each record includes:
 * <ul>
 *     <li>Timestamp of the simulation</li>
 *     <li>Total simulation time</li>
 *     <li>Total number of customers served</li>
 *     <li>Average waiting time</li>
 * </ul>
 * </p>
 * <p>
 * Provides methods to append a new summary to the history and to read all
 * past summaries.
 * </p>
 */
public class SimulationHistoryRepository {

    private static final Path HISTORY_FILE = Path.of("output", "simulation-history.csv");
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.US);
    /**
     * Appends a simulation summary to the history CSV file.
     * <p>
     * If the history file does not exist, it will be created along with the parent
     * directories, and a header row will be added.
     *
     * @param summary the simulation summary to append
     * @throws IOException if an I/O error occurs while writing
     */
    public void append(SimulationSummary summary) throws IOException {
        Path parent = HISTORY_FILE.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        boolean writeHeader = Files.notExists(HISTORY_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(HISTORY_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (writeHeader) {
                writer.write("timestamp;simulationTime;totalCustomers;averageWaiting");
                writer.newLine();
            }
            LocalDateTime now = LocalDateTime.now();
            writer.write(String.format(Locale.US, "%s;%.3f;%d;%.3f",
                    now.format(TIMESTAMP_FORMAT),
                    summary.simulationTime(),
                    summary.totalCustomersServed(),
                    summary.averageWaitingTime()));
            writer.newLine();
        }
    }
    /**
     * Reads all simulation history entries from the CSV file.
     *
     * @return a list of {@link SimulationHistoryEntry}; returns an empty list
     *         if the history file does not exist
     * @throws IOException if an I/O error occurs while reading
     */
    public List<SimulationHistoryEntry> readAll() throws IOException {
        if (Files.notExists(HISTORY_FILE)) {
            return List.of();
        }
        List<String> lines = Files.readAllLines(HISTORY_FILE, StandardCharsets.UTF_8);
        return lines.stream()
                .skip(1) // header
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::parseLine)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private SimulationHistoryEntry parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            return SimulationHistoryEntry.empty();
        }
        try {
            LocalDateTime timestamp = LocalDateTime.parse(parts[0], TIMESTAMP_FORMAT);
            double simTime = Double.parseDouble(parts[1]);
            int total = Integer.parseInt(parts[2]);
            double avgWait = Double.parseDouble(parts[3]);
            SimulationSummary summary = new SimulationSummary(simTime, total, avgWait);
            return new SimulationHistoryEntry(timestamp, summary);
        } catch (Exception e) {
            return SimulationHistoryEntry.empty();
        }
    }
    /**
     * Represents a single simulation history entry.
     *
     * @param timestamp the date and time when the simulation was run
     * @param summary the summary of the simulation run
     */
    public record SimulationHistoryEntry(LocalDateTime timestamp, SimulationSummary summary) {
        /**
         * Returns an empty history entry.
         *
         * @return an empty {@link SimulationHistoryEntry} with default values
         */
        public static SimulationHistoryEntry empty() {
            return new SimulationHistoryEntry(LocalDateTime.MIN, new SimulationSummary(0, 0, 0));
        }

        public boolean isEmpty() {
            return timestamp.equals(LocalDateTime.MIN);
        }
        /**
         * Returns a human-readable string representation of the entry.
         *
         * @return formatted string including timestamp, total customers, average
         *         waiting time, and simulation time
         */
        public String toDisplayString() {
            SimulationSummary sum = summary;
            return String.format("%s – %d customers, avg wait %s min (sim %s min)",
                    timestamp.format(TIMESTAMP_FORMAT),
                    sum.totalCustomersServed(),
                    sum.formattedAverageWaitingTime(),
                    sum.formattedSimulationTime());
        }
    }
}

