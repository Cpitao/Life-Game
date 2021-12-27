package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.SimulationEngine;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ControlPanel {

    private App app;

    private GridPane layoutGrid = new GridPane();
    private double controlPanelWidth;

    public ControlPanel(App app, double controlPanelWidth)
    {
        this.app = app;
        this.controlPanelWidth = controlPanelWidth;
        setUpControlPanelLayout();
    }

    private void setUpControlPanelLayout()
    {
        layoutGrid.setPrefWidth(controlPanelWidth);

        // start / stop buttons
        Button leftMapRunButton = getStartStopButton(app.getLeftEngine());
        Button rightMapRunButton = getStartStopButton(app.getRightEngine());
        layoutGrid.add(leftMapRunButton, 0, 0);
        layoutGrid.add(rightMapRunButton, 1, 0);
        // end of start/stop buttons

        // speed controls
        HBox leftSpeedControlsBox = getSpeedControls(app.getLeftEngine());
        HBox rightSpeedControlsBox = getSpeedControls(app.getRightEngine());
        layoutGrid.add(leftSpeedControlsBox, 0, 1);
        layoutGrid.add(rightSpeedControlsBox, 1, 1);
        // end of speed controls
    }

    public GridPane getLayoutGrid()
    {
        return layoutGrid;
    }

    public Button getStartStopButton(SimulationEngine engine)
    {
        Button startStopButton = new Button("Run");
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.changeRunningState();
                if (!engine.isRunning())
                {
                    startStopButton.setText("Resume");
                }
                else
                {
                    synchronized (engine.lock)
                    {
                        engine.lock.notify();
                    }
                    startStopButton.setText("Pause");
                }
            }
        });
        startStopButton.setPrefWidth(controlPanelWidth / 2);
        GridPane.setFillWidth(startStopButton, true);
        GridPane.setHalignment(startStopButton, HPos.CENTER);
        return startStopButton;
    }

    public HBox getSpeedControls(SimulationEngine engine)
    {
        Label speedLabel = new Label("Move delay\n" + engine.getMoveDelay() + "ms");
        speedLabel.setFont(new Font(12));
        speedLabel.setMinWidth(controlPanelWidth / 6);
        speedLabel.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(speedLabel, HPos.CENTER);

        Button lowerDelayButton = new Button("-");
        lowerDelayButton.setFont(new Font(20));
        lowerDelayButton.setPrefWidth(controlPanelWidth / 6);
        lowerDelayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.increaseSpeed();
                speedLabel.setText("Move delay\n"
                        + engine.getMoveDelay() + "ms");
                speedLabel.setFont(new Font(12));
            }
        });

        Button increaseDelayButton = new Button("+");
        increaseDelayButton.setFont(new Font(20));
        increaseDelayButton.setPrefWidth(controlPanelWidth/6);
        increaseDelayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.decreaseSpeed();
                speedLabel.setText("Move delay\n"
                        + engine.getMoveDelay() + "ms");
                speedLabel.setFont(new Font(12));
            }
        });


        HBox speedControlBox = new HBox();
        speedControlBox.getChildren().addAll(increaseDelayButton, speedLabel,
                lowerDelayButton);

        return speedControlBox;
    }


}
