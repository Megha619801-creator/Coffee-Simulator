package simulation.logic;

public class SimulatorMain {
    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.initialize();
        sim.run(50.0); // simulate until clock = 50
    }
}
