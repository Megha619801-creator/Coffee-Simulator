package simulation.demo;

import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link DistributionDemo} class and the probability
 * distributions it demonstrates.
 * <p>
 * These tests verify that samples generated from the negative exponential
 * and normal distributions are valid and that the demo's main method
 * executes without throwing exceptions.
 */
class DistributionDemoTest {
    /**
     * Verifies that samples generated from a {@link Negexp} distribution
     * are finite and non-negative.
     */
    @Test
    void negexpSamplesAreFinite() {
        Negexp exp = new Negexp(5.0, System.currentTimeMillis());

        for (int i = 0; i < 10; i++) {
            double sample = exp.sample();
            assertTrue(Double.isFinite(sample), "Negexp sample should be finite");
            assertTrue(sample >= 0.0, "Negexp sample should be non-negative");
        }
    }
    /**
     * Verifies that samples generated from a {@link Normal} distribution
     * are finite.
     * <p>
     * Normal distributions may produce negative values, so only finiteness
     * is checked.
     */
    @Test
    void normalSamplesAreFinite() {
        Normal normal = new Normal(0.0, 1.0, System.currentTimeMillis());

        for (int i = 0; i < 10; i++) {
            double sample = normal.sample();
            assertTrue(Double.isFinite(sample), "Normal sample should be finite");
            // Normal distribution can be negative, so just check it's finite
        }
    }
    /**
     * Ensures that the {@link DistributionDemo#main(String[])} method
     * runs without throwing any exceptions.
     */
    @Test
    void mainMethodRunsWithoutException() {
        // Just ensure the demo main method executes without throwing
        assertDoesNotThrow(() -> DistributionDemo.main(new String[0]));
    }
}
