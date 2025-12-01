package simulation.logic;

import simulation.config.SimulationParameters;
import simulation.data.FileManager;

import java.io.IOException;
import java.nio.file.Path;

public class SimulatorMain {
    public static void main(String[] args) {
        SimulationParameters params = loadParameters();
        Simulator sim = new Simulator(params);
        sim.initialize();
        sim.run();
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
}
