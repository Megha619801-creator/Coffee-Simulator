package simulation.model;

import eduni.distributions.ContinuousGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for {@link EventList}.
 *
 * <p>Ensures events are ordered correctly by time
 * and retrieved in priority order.</p>
 */
class EventListTest {

    private EventList list;

    @BeforeEach
    void setUp() {
        list = new EventList();
    }

    @Test
    void testAddAndRemoveOrder() {
        // create a simple service point with a trivial generator
        ContinuousGenerator gen = new ContinuousGenerator() {
            private long seed = 1L;
            @Override
            public double sample() { return 1.0; }
            @Override
            public void setSeed(long seed) { this.seed = seed; }
            @Override
            public long getSeed() { return seed; }
            @Override
            public void reseed() { seed = seed + 1; }
        };

        ServicePoint sp = new ServicePoint("SP1", gen);
        Customer c1 = new Customer("INSTORE", 0.0);
        Customer c2 = new Customer("INSTORE", 0.0);

        Event e1 = new Event(1.0, Event.ARRIVAL, c1, sp);
        Event e2 = new Event(2.0, Event.ARRIVAL, c2, sp);

        assertTrue(list.isEmpty());
        list.add(e2);
        list.add(e1);

        assertFalse(list.isEmpty());
        assertSame(e1, list.peekNext());
        assertSame(e1, list.removeNext());
        assertSame(e2, list.removeNext());
        assertTrue(list.isEmpty());
    }
}
