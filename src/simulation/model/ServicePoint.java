package simulation.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ServicePoint {
    private final String name;
    private final Queue<Customer> queue = new LinkedList<>();
    private boolean busy = false;
    private final Random rand = new Random();
    private final double meanServiceTime;

    public ServicePoint(String name, double meanServiceTime) {
        this.name = name;
        this.meanServiceTime = meanServiceTime;
    }

    public String getName() {
        return name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void addCustomer(Customer c) {
        queue.add(c);
    }

    public Customer getNextCustomer() {
        return queue.poll();
    }

    public boolean hasWaitingCustomer() {
        return !queue.isEmpty();
    }

    public double generateServiceTime() {
        // Exponential distribution
        return -meanServiceTime * Math.log(1 - rand.nextDouble());
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public int getQueueLength() {
        return queue.size();
    }
}
