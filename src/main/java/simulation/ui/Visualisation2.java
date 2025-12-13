package simulation.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import simulation.model.Customer;

import java.util.HashMap;
import java.util.Map;
/**
 * Simple visualisation of the coffee shop simulation.
 * <p>
 * Displays service points as vertical columns and customers as red circles with labels.
 * Customers are stacked vertically per service point and their positions update dynamically.
 * </p>
 */
public class Visualisation2 extends Canvas implements IVisualisation {
    private final GraphicsContext gc;
    private final Map<Customer, Integer> customerPositions = new HashMap<>();
    private final int[] spYPositions = new int[4];
    /**
     * Constructs a visualisation canvas with given width and height.
     *
     * @param w initial canvas width
     * @param h initial canvas height
     */
    public Visualisation2(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }
    /**
     * Clears the canvas and resets all customer positions.
     * Service point Y positions are reset for vertical stacking.
     */
    @Override
    public void clearDisplay() {
        gc.setFill(Color.YELLOW);
        gc.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < spYPositions.length; i++) {
            spYPositions[i] = 20;
        }
        customerPositions.clear();
    }
    /**
     * Adds a customer to a specific service point column.
     * The customer is drawn as a red circle with a label.
     *
     * @param c                the customer to add
     * @param servicePointIndex index of the service point (0–3)
     */
    @Override
    public void addCustomer(Customer c, int servicePointIndex) {
        int x = 50 + servicePointIndex * 100;
        int y = spYPositions[servicePointIndex];
        drawCustomer(c, x, y);
        customerPositions.put(c, servicePointIndex);
        spYPositions[servicePointIndex] += 20;
    }
    /**
     * Moves a customer to a new service point column.
     * If the customer does not exist yet, they are added.
     *
     * @param c                the customer to move
     * @param servicePointIndex index of the target service point
     */
    @Override
    public void moveCustomer(Customer c, int servicePointIndex) {
        if (!customerPositions.containsKey(c)) {
            addCustomer(c, servicePointIndex);
            return;
        }
        customerPositions.put(c, servicePointIndex);
        redrawAll();
    }
    /**
     * Removes a customer from the visualisation and redraws all remaining customers.
     *
     * @param c the customer to remove
     */
    @Override
    public void removeCustomer(Customer c) {
        customerPositions.remove(c);
        redrawAll();
    }

    private void redrawAll() {
        clearDisplay();
        for (Map.Entry<Customer, Integer> entry : customerPositions.entrySet()) {
            int spIndex = entry.getValue();
            int x = 50 + spIndex * 100;
            int y = spYPositions[spIndex];
            drawCustomer(entry.getKey(), x, y);
            spYPositions[spIndex] += 20;
        }
    }
    /**
     * Draws a single customer as a red circle with a label above it.
     *
             * @param customer the customer to draw
     * @param x        X-coordinate for drawing
     * @param y        Y-coordinate for drawing
     */
    private void drawCustomer(Customer customer, int x, int y) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, 15, 15);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(12));
        gc.fillText("C" + customer.getId(), x, y - 2);
    }
}
