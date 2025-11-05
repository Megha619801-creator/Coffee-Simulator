package simulation.logic;

import simulation.model.*;
import java.util.Random;

public class Simulator {
    private final EventList eventList = new EventList();
    private double clock = 0.0;

    private final ServicePoint cashier = new ServicePoint("Cashier", 3.0);
    private final ServicePoint barista = new ServicePoint("Barista", 5.0);
    private final Random rand = new Random();

    private final double meanArrivalTime = 4.0; // mean time between arrivals

    public void initialize() {
        double firstArrival = generateArrivalTime();
        Customer first = new Customer("INSTORE", firstArrival);
        eventList.add(new Event(firstArrival, Event.ARRIVAL, first, cashier));
    }

    public void run(double endTime) {
        while (!eventList.isEmpty() && clock < endTime) {
            Event e = eventList.removeNext();
            clock = e.getTime();
            handleEvent(e);
        }
    }

    private void handleEvent(Event e) {
        switch (e.getType()) {
            case Event.ARRIVAL -> handleArrival(e);
            case Event.DEPARTURE -> handleDeparture(e);
        }
    }

    private void handleArrival(Event e) {
        Customer c = e.getCustomer();
        ServicePoint sp = e.getTarget();
        System.out.println(e);

        if (!sp.isBusy()) {
            sp.setBusy(true);
            c.setServiceStartTime(clock);
            double serviceTime = sp.generateServiceTime();
            c.setServiceEndTime(clock + serviceTime);
            eventList.add(new Event(clock + serviceTime, Event.DEPARTURE, c, sp));
        } else {
            sp.addCustomer(c);
        }

        // Schedule next arrival
        double nextArrival = clock + generateArrivalTime();
        Customer next = new Customer("INSTORE", nextArrival);
        eventList.add(new Event(nextArrival, Event.ARRIVAL, next, cashier));
    }

    private void handleDeparture(Event e) {
        Customer c = e.getCustomer();
        ServicePoint sp = e.getTarget();
        System.out.println(e + " (Wait=" + c.getWaitingTime() + ", Service=" + c.getServiceTime() + ")");

        if (sp.hasWaitingCustomer()) {
            Customer next = sp.getNextCustomer();
            next.setServiceStartTime(clock);
            double serviceTime = sp.generateServiceTime();
            next.setServiceEndTime(clock + serviceTime);
            eventList.add(new Event(clock + serviceTime, Event.DEPARTURE, next, sp));
        } else {
            sp.setBusy(false);
        }

        // move from cashier → barista
        if (sp == cashier) {
            eventList.add(new Event(clock, Event.ARRIVAL, c, barista));
        }
    }

    private double generateArrivalTime() {
        // Poisson process = exponential inter-arrival time
        return -meanArrivalTime * Math.log(1 - rand.nextDouble());
    }
}
