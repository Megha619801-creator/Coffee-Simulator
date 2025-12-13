package simulation.model;

import java.util.PriorityQueue;
/**
 * Maintains a prioritized list of future simulation events.
 * <p>
 * Events are ordered by their scheduled execution time.
 */
public class EventList {
    private final PriorityQueue<Event> list = new PriorityQueue<>();
    /**
     * Adds a new event to the event list.
     *
     * @param e event to add
     */
    public void add(Event e) {
        list.add(e);
    }

    public Event removeNext() {
        return list.poll();
    }

    public Event peekNext() {
        return list.peek();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
