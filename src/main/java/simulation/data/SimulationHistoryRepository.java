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

public class SimulationHistoryRepository {

    private static final Path HISTORY_FILE = Path.of("output", "simulation-history.csv");
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.US);

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

    public record SimulationHistoryEntry(LocalDateTime timestamp, SimulationSummary summary) {

        public static SimulationHistoryEntry empty() {
            return new SimulationHistoryEntry(LocalDateTime.MIN, new SimulationSummary(0, 0, 0));
        }

        public boolean isEmpty() {
            return timestamp.equals(LocalDateTime.MIN);
        }

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

