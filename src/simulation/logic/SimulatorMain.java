package simulation.logic;

public class SimulatorMain {
    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.initialize();
        sim.run(15.0); // simulate 100 time units
    }
}
