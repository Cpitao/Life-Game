package agh.ics.oop.GuiStats;

import agh.ics.oop.App;
import agh.ics.oop.IGUIChangeObserver;
import agh.ics.oop.StatTrackers.MapStatsTracker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class StatsPanel {

    private App app;
    private double controlPanelWidth;
    private MapStatsTracker leftMapStatsTracker;
    private MapStatsTracker rightMapStatsTracker;

    private GridPane statsGrid;

    private GenesModeDisplay leftGenesModesDisplay;
    private GenesModeDisplay rightGenesModesDisplay;

    private HBox countChartBox;
    private AnimalCountDisplay animalCountDisplay;
    private PlantCountDisplay plantCountDisplay;

    private LinkedList<IGUIChangeObserver> newEraGUIObservers = new LinkedList<>();

    public StatsPanel(App app, MapStatsTracker leftMapStatsTracker,
                      MapStatsTracker rightMapStatsTracker, double controlPanelWidth)
    {
        this.app = app;
        this.leftMapStatsTracker = leftMapStatsTracker;
        this.rightMapStatsTracker = rightMapStatsTracker;

        leftMapStatsTracker.assignGUIPanel(this);
        rightMapStatsTracker.assignGUIPanel(this);

        this.controlPanelWidth = controlPanelWidth;
        this.init();
    }

    public void init()
    {
        statsGrid = new GridPane();
//        statsGrid.setGridLinesVisible(true);
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));

        leftGenesModesDisplay = new GenesModeDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getGenesStatsTracker());
        newEraGUIObservers.add(leftGenesModesDisplay);
        rightGenesModesDisplay = new GenesModeDisplay(app, controlPanelWidth,
                rightMapStatsTracker.getGenesStatsTracker());
        newEraGUIObservers.add(rightGenesModesDisplay);

        statsGrid.add(leftGenesModesDisplay.getView(), 0, 0);
        statsGrid.add(rightGenesModesDisplay.getView(), 1, 0);

        countChartBox = new HBox();

        animalCountDisplay = new AnimalCountDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getAnimalCountTracker(), rightMapStatsTracker.getAnimalCountTracker());

//        statsGrid.add(animalCountDisplay.getChart(), 0, 1, 2, 1);
        newEraGUIObservers.add(animalCountDisplay);

        countChartBox.getChildren().add(animalCountDisplay.getChart());

        plantCountDisplay = new PlantCountDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getPlantCountTracker(), rightMapStatsTracker.getPlantCountTracker());
//        statsGrid.add(plantCountDisplay.getChart(), 0, 2, 2, 1);
        newEraGUIObservers.add(plantCountDisplay);

        HBox countChoiceBox = new HBox();
        countChoiceBox.setAlignment(Pos.CENTER);
        countChoiceBox.setPrefWidth(controlPanelWidth);
        countChoiceBox.setPrefHeight(app.getWindowHeight() / 40);

        ToggleGroup countChoiceGroup = new ToggleGroup();
        RadioButton animalRadioButton = new RadioButton("Animals count");
        animalRadioButton.setUserData("animals");
        RadioButton plantRadioButton = new RadioButton("Plants count");
        plantRadioButton.setUserData("plants");
        animalRadioButton.setSelected(true);
        animalRadioButton.setToggleGroup(countChoiceGroup);
        plantRadioButton.setToggleGroup(countChoiceGroup);

        countChoiceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (countChoiceGroup.getSelectedToggle().getUserData().toString().equals("animals"))
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(animalCountDisplay.getChart());
                }
                else
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(plantCountDisplay.getChart());
                }
            }
        });
        countChoiceBox.getChildren().addAll(animalRadioButton, plantRadioButton);

        statsGrid.add(countChoiceBox, 0, 1, 2, 1);

        statsGrid.add(countChartBox, 0, 2, 2, 1);
        newEraGUIUpdates();
    }

    public GridPane getStatsGrid()
    {
        return statsGrid;
    }

    public void newEraGUIUpdates()
    {
        for (IGUIChangeObserver observer: newEraGUIObservers)
        {
            observer.updateGUI();
        }
    }
}
