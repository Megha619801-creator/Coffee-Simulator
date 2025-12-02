package simulation.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import simu.framework.Trace;
import simu.framework.Trace.Level;

import java.text.DecimalFormat;

public class MainApp extends Application implements ISimulatorUI {

    private IControllerVtoM controller;
    private TextField time;
    private TextField delay;
    private Label results;
    private Label statusValue;
    private Label currentSpeedLabel;
    private Button startButton;
    private Button slowButton;
    private Button speedUpButton;
    private Button pauseButton;
    private Button resumeButton;
    private Button stepButton;
    private IVisualisation display;

    @Override
    public void init() {
        Trace.setTraceLevel(Level.INFO);
        controller = new Controller(this);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        startButton = new Button("Start / Restart");
        startButton.setTooltip(new Tooltip("Begin a new simulation run with the entered settings."));
        startButton.setOnAction(event -> {
            if (controller.isSimulationRunning()) {
                return;
            }
            controller.startSimulation();
            onSimulationStarted();
        });

        slowButton = new Button("Slower animation");
        slowButton.setTooltip(new Tooltip("Increase delay by 10% to slow down visuals."));
        slowButton.setOnAction(e -> controller.decreaseSpeed());

        speedUpButton = new Button("Faster animation");
        speedUpButton.setTooltip(new Tooltip("Decrease delay by 10% to speed up visuals."));
        speedUpButton.setOnAction(e -> controller.increaseSpeed());

        pauseButton = new Button("Pause");
        pauseButton.setTooltip(new Tooltip("Temporarily halt progression while keeping the current state."));
        pauseButton.setDisable(true);
        pauseButton.setOnAction(e -> {
            if (!controller.isSimulationRunning()) {
                return;
            }
            controller.pauseSimulation();
            onSimulationPaused();
        });

        resumeButton = new Button("Resume");
        resumeButton.setTooltip(new Tooltip("Continue the simulation after a pause."));
        resumeButton.setDisable(true);
        resumeButton.setOnAction(e -> {
            if (!controller.isSimulationRunning()) {
                return;
            }
            controller.resumeSimulation();
            onSimulationResumed();
        });

        stepButton = new Button("Step once");
        stepButton.setTooltip(new Tooltip("Execute a single simulation event for inspection."));
        stepButton.setDisable(true);
        stepButton.setOnAction(e -> {
            if (!controller.isSimulationRunning()) {
                controller.startSimulation();
                onSimulationStarted();
            }
            controller.pauseSimulation();
            controller.stepSimulation();
            onSimulationPaused();
        });

        time = new TextField("200");
        time.setPrefWidth(150);
        time.setMaxWidth(150);
        time.setAlignment(Pos.CENTER_RIGHT);
        delay = new TextField("10");
        delay.setPrefWidth(150);
        delay.setMaxWidth(150);
        delay.setAlignment(Pos.CENTER_RIGHT);
        delay.textProperty().addListener((obs, oldVal, newVal) -> renderManualSpeed(newVal));
        results = new Label();
        statusValue = new Label("Idle");
        statusValue.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        currentSpeedLabel = new Label();
        currentSpeedLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        currentSpeedLabel.setStyle("-fx-text-fill: #333333;");

        display = new Visualisation(450, 250);

        Label description = new Label(
                "Simulates a single coffee shop where customers move through cashier, barista, finishing, and pickup stages.");
        description.setWrapText(true);
        description.setMaxWidth(260);

        Label settingsTitle = new Label("Simulation Settings");
        settingsTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        Label timeLabel = new Label("How long to simulate (min):");
        timeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        Label delayLabel = new Label("Animation speed (ms):");
        delayLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        Label animationHint = new Label("Higher value = slower movement updates.");
        animationHint.setFont(Font.font("Tahoma", FontWeight.NORMAL, 11));
        animationHint.setWrapText(true);
        animationHint.setMaxWidth(180);

        GridPane settingsGrid = new GridPane();
        settingsGrid.setVgap(14);
        settingsGrid.setHgap(10);
        settingsGrid.setPadding(new Insets(8, 0, 12, 0));
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setHalignment(HPos.RIGHT);
        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setPrefWidth(150);
        fieldColumn.setHgrow(Priority.ALWAYS);
        settingsGrid.getColumnConstraints().addAll(labelColumn, fieldColumn);
        settingsGrid.add(timeLabel, 0, 0);
        settingsGrid.add(time, 1, 0);
        settingsGrid.add(delayLabel, 0, 1);
        settingsGrid.add(delay, 1, 1);
        settingsGrid.add(animationHint, 0, 2, 2, 1);

        Label controlsTitle = new Label("Controls");
        controlsTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        Label stateControlsLabel = new Label("Simulation state");
        stateControlsLabel.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 13));
        HBox stateButtonsRow = new HBox(10, startButton, pauseButton, resumeButton);
        stateButtonsRow.setAlignment(Pos.CENTER_LEFT);

        HBox stepRow = new HBox(stepButton);
        stepRow.setAlignment(Pos.CENTER_LEFT);

        Label speedControlsLabel = new Label("Animation speed");
        speedControlsLabel.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 13));
        HBox speedButtonsRow = new HBox(10, speedUpButton, slowButton);
        speedButtonsRow.setAlignment(Pos.CENTER_LEFT);

        VBox controlsSection = new VBox(10);
        controlsSection.setPadding(new Insets(5, 0, 12, 0));
        controlsSection.getChildren().addAll(
                stateControlsLabel,
                stateButtonsRow,
                stepRow,
                speedControlsLabel,
                speedButtonsRow,
                currentSpeedLabel);

        Label resultsTitle = new Label("Results");
        resultsTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        Label totalLabel = new Label("Simulation finished after (min):");
        totalLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        Label statusLabel = new Label("Status:");
        statusLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        GridPane resultsGrid = new GridPane();
        resultsGrid.setAlignment(Pos.TOP_LEFT);
        resultsGrid.setPadding(new Insets(5, 0, 0, 0));
        resultsGrid.setHgap(10);
        resultsGrid.setVgap(8);
        resultsGrid.add(totalLabel, 0, 0);
        resultsGrid.add(results, 1, 0);
        resultsGrid.add(statusLabel, 0, 1);
        resultsGrid.add(statusValue, 1, 1);

        VBox leftPane = new VBox(16);
        leftPane.setPadding(new Insets(15));
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.getChildren()
                .addAll(description, settingsTitle, settingsGrid, controlsTitle, controlsSection, resultsTitle,
                        resultsGrid);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(leftPane, (Canvas) display);

        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coffee Shop Simulation");
        primaryStage.show();

        onSimulationStopped();
        updateStatus("Idle");
        renderManualSpeed(delay.getText());
    }

    @Override
    public double getTime() {
        return Double.parseDouble(time.getText());
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    @Override
    public void setEndingTime(double time) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        results.setText(formatter.format(time));
        updateStatus("Finished");
        onSimulationStopped();
    }

    @Override
    public IVisualisation getVisualisation() {
        return display;
    }

    @Override
    public void showCurrentDelay(long delayValue) {
        Platform.runLater(() -> {
            delay.setText(String.valueOf(delayValue));
            currentSpeedLabel.setText(formatDelayText(delayValue));
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void onSimulationStarted() {
        startButton.setDisable(true);
        slowButton.setDisable(false);
        speedUpButton.setDisable(false);
        pauseButton.setDisable(false);
        resumeButton.setDisable(true);
        stepButton.setDisable(true);
        updateStatus("Running");
    }

    private void onSimulationPaused() {
        pauseButton.setDisable(true);
        resumeButton.setDisable(false);
        stepButton.setDisable(false);
        slowButton.setDisable(false);
        speedUpButton.setDisable(false);
        updateStatus("Paused");
    }

    private void onSimulationResumed() {
        pauseButton.setDisable(false);
        resumeButton.setDisable(true);
        stepButton.setDisable(true);
        slowButton.setDisable(false);
        speedUpButton.setDisable(false);
        updateStatus("Running");
    }

    private void onSimulationStopped() {
        startButton.setDisable(false);
        slowButton.setDisable(true);
        speedUpButton.setDisable(true);
        pauseButton.setDisable(true);
        resumeButton.setDisable(true);
        stepButton.setDisable(true);
    }

    private void updateStatus(String state) {
        statusValue.setText(state);
    }

    private void renderManualSpeed(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            currentSpeedLabel.setText("Enter animation speed (ms)");
            return;
        }
        try {
            long parsed = Long.parseLong(candidate);
            currentSpeedLabel.setText(formatDelayText(parsed));
        } catch (NumberFormatException ex) {
            currentSpeedLabel.setText("Invalid speed value");
        }
    }

    private String formatDelayText(long delayValue) {
        return delayValue + " ms per step";
    }
}
