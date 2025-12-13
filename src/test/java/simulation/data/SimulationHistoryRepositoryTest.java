package simulation.data;

import org.junit.jupiter.api.*;
import simulation.ui.SimulationSummary;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link SimulationHistoryRepository}.
 * <p>
 * These tests verify that simulation summaries can be correctly appended
 * to and read from the CSV history file, including handling of empty
 * or invalid files.
 */
class SimulationHistoryRepositoryTest {

    private SimulationHistoryRepository repo;
    private static final Path TEST_FILE = Path.of("output", "simulation-history.csv");
    /**
     * Sets up a fresh {@link SimulationHistoryRepository} before each test
     * and deletes any existing test CSV file to ensure tests start clean.
     *
     * @throws IOException if the test file cannot be deleted
     */
    @BeforeEach
    void setup() throws IOException {
        repo = new SimulationHistoryRepository();
        // Delete any existing test file before running each test
        if (Files.exists(TEST_FILE)) {
            Files.delete(TEST_FILE);
        }
    }
    /**
     * Tests that a single {@link SimulationSummary} can be appended to the
     * CSV and read back correctly.
     *
     * @throws IOException if an I/O error occurs during append or read
     */
    @Test
    void appendAndReadSingleEntry() throws IOException {
        SimulationSummary summary = new SimulationSummary(5.5, 12, 1.75);

        repo.append(summary);
        List<SimulationHistoryRepository.SimulationHistoryEntry> entries = repo.readAll();

        assertEquals(1, entries.size(), "CSV should contain exactly one entry");

        SimulationHistoryRepository.SimulationHistoryEntry entry = entries.get(0);

        assertFalse(entry.isEmpty(), "Entry should not be empty");
        assertEquals(12, entry.summary().totalCustomersServed());
        assertEquals(5.5, entry.summary().simulationTime(), 1e-9);
        assertEquals(1.75, entry.summary().averageWaitingTime(), 1e-9);
    }
    /**
     * Tests that reading a non-existent CSV file returns an empty list.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    void readEmptyWhenFileMissing() throws IOException {
        // Ensure file does not exist
        if (Files.exists(TEST_FILE)) Files.delete(TEST_FILE);

        List<SimulationHistoryRepository.SimulationHistoryEntry> entries = repo.readAll();

        assertTrue(entries.isEmpty(), "Reading nonexistent file should return empty list");
    }
    /**
     * Tests that a corrupted CSV line is parsed into an empty
     * {@link SimulationHistoryRepository.SimulationHistoryEntry}.
     *
     * @throws IOException if an I/O error occurs while writing test file
     */
    @Test
    void parseInvalidLineReturnsEmpty() throws IOException {
        // Create corrupted file manually
        Files.createDirectories(TEST_FILE.getParent());
        Files.writeString(TEST_FILE,
                "timestamp;simulationTime;totalCustomers;averageWaiting\n" + // header
                        "INVALID DATA LINE\n",
                StandardOpenOption.CREATE);

        List<SimulationHistoryRepository.SimulationHistoryEntry> entries = repo.readAll();

        assertEquals(1, entries.size());
        assertTrue(entries.get(0).isEmpty(), "Invalid CSV line should produce empty entry");
    }

}
