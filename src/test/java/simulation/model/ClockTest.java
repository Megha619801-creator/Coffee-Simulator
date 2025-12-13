package simulation.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link Clock} singleton.
 *
 * <p>
 * Verifies time reset, advancement, and validation logic.
 * </p>
 */
class ClockTest {

    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.getInstance();
        clock.reset();
    }
    /**
     * Tests resetting the clock and setting time explicitly.
     */

    @Test
    void testResetAndSetTime() {
        assertEquals(0.0, clock.getTime(), 1e-9);
        clock.setTime(5.5);
        assertEquals(5.5, clock.getTime(), 1e-9);
    }
    /**
     * Tests advancing the clock forward by a positive amount.
     */

    @Test
    void testAdvance() {
        clock.setTime(1.0);
        clock.advance(2.25);
        assertEquals(3.25, clock.getTime(), 1e-9);
    }
    /**
     * Tests that invalid time values throw exceptions.
     */

    @Test
    void testInvalidSetAdvance() {
        assertThrows(IllegalArgumentException.class, () -> clock.setTime(-1.0));
        assertThrows(IllegalArgumentException.class, () -> clock.advance(-0.5));
    }
}
