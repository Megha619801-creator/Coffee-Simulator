package simulation.random;

import org.junit.jupiter.api.Test;
import simulation.model.Customer;
import simulation.model.Event;
import simulation.model.EventList;
import simulation.model.ServicePoint;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link ArrivalProcess} class.
 * <p>
 * Verifies that scheduling the next arrival:
 * <ul>
 *   <li>Adds an {@link Event} to the {@link EventList}</li>
 *   <li>Creates an ARRIVAL event at the correct future time</li>
 *   <li>Associates the event with the correct {@link ServicePoint}</li>
 *   <li>Initializes the {@link Customer} with the correct type and arrival time</li>
 * </ul>
 * A deterministic generator is used to ensure predictable and repeatable behavior.
 */
class ArrivalProcessTest {

    @Test
    void scheduleNextAddsArrivalWithExpectedTimeAndCustomer() {
        EventList eventList = new EventList();
        ServicePoint sp = new ServicePoint("Entry", new DeterministicGenerator(1.0));
        ArrivalProcess process = new ArrivalProcess("INSTORE", sp, new DeterministicGenerator(2.0));

        double now = 5.0;
        process.scheduleNext(now, eventList);

        assertFalse(eventList.isEmpty());
        Event next = eventList.removeNext();
        assertEquals(now + 2.0, next.getTime(), 1e-9);
        assertEquals(Event.ARRIVAL, next.getType());
        assertSame(sp, next.getTarget());

        Customer c = next.getCustomer();
        assertEquals("INSTORE", c.getType());
        assertEquals(now + 2.0, c.getArrivalTime(), 1e-9);
    }
}

