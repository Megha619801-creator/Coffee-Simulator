package simulation.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClockTest {

    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.getInstance();
        clock.reset();
    }

    @Test
    void testResetAndSetTime() {
        assertEquals(0.0, clock.getTime(), 1e-9);
        clock.setTime(5.5);
        assertEquals(5.5, clock.getTime(), 1e-9);
    }

    @Test
    void testAdvance() {
        clock.setTime(1.0);
        clock.advance(2.25);
        assertEquals(3.25, clock.getTime(), 1e-9);
    }

    @Test
    void testInvalidSetAdvance() {
        assertThrows(IllegalArgumentException.class, () -> clock.setTime(-1.0));
        assertThrows(IllegalArgumentException.class, () -> clock.advance(-0.5));
    }
}
