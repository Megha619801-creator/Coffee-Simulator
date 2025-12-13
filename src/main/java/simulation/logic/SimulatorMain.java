package simulation.logic;

import simulation.config.SimulationParameters;
import simulation.data.FileManager;
import simulation.statistics.SimulationStatistics;
import simulation.statistics.StatisticsReporter;

import java.io.IOException;
import java.nio.file.Path;
/**
 * Entry point for running the coffee shop simulation from the command line.
 * <p>
 * This class performs the following steps:
 * <ol>
 *     <li>Loads simulation parameters from a configuration file (or uses defaults if not found).</li>
 *     <li>Initializes and runs the {@link Simulator}.</li>
 *     <li>Writes the resulting statistics to a CSV file and prints a summary to the console.</li>
 * </ol>
 * </p>
 */
public class SimulatorMain {
    /**
     * Main method to start the simulation.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        SimulationParameters params = loadParameters();
        Simulator sim = new Simulator(params);
        sim.initialize();
        sim.run();
        writeResults(sim);
    }

    private static SimulationParameters loadParameters() {
        Path configPath = Path.of("config", "simulator.properties");
        if (configPath.toFile().exists()) {
            try {
                return FileManager.loadParameters(configPath);
            } catch (IOException e) {
                System.err.println("Failed to load parameters from file, using defaults: " + e.getMessage());
            }
        }
        return SimulationParameters.defaults();
    }

    private static void writeResults(Simulator simulator) {
        SimulationStatistics statistics = simulator.getStatistics();
        Path outputPath = Path.of("output", "simulation-results.csv");
        try {
            FileManager.writeStatistics(outputPath, statistics);
            System.out.println("Simulation results written to " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write simulation results: " + e.getMessage());
        }
        StatisticsReporter.printToConsole(statistics);
    }
}
