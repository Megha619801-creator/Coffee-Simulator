package simulation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link Event}.
 *
 * <p>
 * Verifies that events are comparable based on time
 * and that string representations contain meaningful information.
 * </p>
 *
 * <p>
 * Ensures correct ordering behavior required by the simulation scheduler.
 * </p>
 */
class EventTest {

    @Test
    void testCompareToAndToString() {
        ServicePoint sp = new ServicePoint("T1", new eduni.distributions.Negexp(1.0));
        Customer c1 = new Customer("INSTORE", 0.0);
        Customer c2 = new Customer("INSTORE", 0.0);

        Event e1 = new Event(1.0, Event.ARRIVAL, c1, sp);
        Event e2 = new Event(2.0, Event.ARRIVAL, c2, sp);

        assertTrue(e1.compareTo(e2) < 0);
        assertTrue(e2.compareTo(e1) > 0);
        assertEquals(0, e1.compareTo(new Event(1.0, Event.ARRIVAL, c1, sp)));

        String s = e1.toString();
        assertTrue(s.contains("ARRIVAL") || s.contains("DEPARTURE"));
        assertTrue(s.contains(sp.getName()));
    }
}
