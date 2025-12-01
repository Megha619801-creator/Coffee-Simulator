package simulation.data;

import simulation.config.SimulationParameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Loads simulation parameters from a properties file.
 */
public class FileManager {

    private FileManager() {
    }

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
}
