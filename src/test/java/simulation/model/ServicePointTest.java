package simulation.model;

import eduni.distributions.ContinuousGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicePointTest {

    private ServicePoint sp;

    @BeforeEach
    void setUp() {
        ContinuousGenerator gen = new ContinuousGenerator() {
            private long seed = 1L;
            @Override
            public double sample() { return 3.14; }
            @Override
            public void setSeed(long seed) { this.seed = seed; }
            @Override
            public long getSeed() { return seed; }
            @Override
            public void reseed() { seed = seed + 1; }
        };
        sp = new ServicePoint("SP-test", gen);
    }

    @Test
    void testQueueOperationsAndBusyFlag() {
        assertEquals("SP-test", sp.getName());
        assertFalse(sp.isBusy());
        assertEquals(0, sp.getQueueLength());
        assertFalse(sp.hasWaitingCustomer());

        Customer c = new Customer("INSTORE", 0.0);
        sp.addCustomer(c);
        assertTrue(sp.hasWaitingCustomer());
        assertEquals(1, sp.getQueueLength());

        Customer next = sp.getNextCustomer();
        assertSame(c, next);
        assertEquals(0, sp.getQueueLength());

        sp.setBusy(true);
        assertTrue(sp.isBusy());
        sp.setBusy(false);
        assertFalse(sp.isBusy());
    }

    @Test
    void testGenerateServiceTime() {
        double s = sp.generateServiceTime();
        assertEquals(3.14, s, 1e-9);
    }
}
