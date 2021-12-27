package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.observers.IGUIChangeObserver;
import agh.ics.oop.stattrackers.MapStatsTracker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.LinkedList;

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
    private AvgEnergyDisplay avgEnergyDisplay;
    private AvgAgeDisplay avgAgeDisplay;
    private AvgKidsDisplay avgKidsDisplay;

    private VBox leftUserSetContainer;
    private VBox rightUserSetContainer;
    private UserSetDisplay leftUserSetDisplay;
    private UserSetDisplay rightUserSetDisplay;


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
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));

        addGenesModesGUI();
        addCountChartsGUI();
        addUserDisplay();

        newEraGUIUpdates();
    }

    private void addGenesModesGUI()
    {
        leftGenesModesDisplay = new GenesModeDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getGenesStatsTracker(), app.getLeftEngine());

        newEraGUIObservers.add(leftGenesModesDisplay);
        rightGenesModesDisplay = new GenesModeDisplay(app, controlPanelWidth,
                rightMapStatsTracker.getGenesStatsTracker(), app.getRightEngine());
        newEraGUIObservers.add(rightGenesModesDisplay);

        ScrollPane leftGenesModesView = leftGenesModesDisplay.getView();
        ScrollPane rightGenesModesView = rightGenesModesDisplay.getView();
        leftGenesModesView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!app.getLeftEngine().isRunning())
                    app.showModeGeneAnimals(app.getLeftEngine(), app.getLeftMapGrid());
            }
        });

        rightGenesModesView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!app.getLeftEngine().isRunning())
                    app.showModeGeneAnimals(app.getRightEngine(), app.getRightMapGrid());
            }
        });

        statsGrid.add(leftGenesModesDisplay.getView(), 0, 0);
        statsGrid.add(rightGenesModesDisplay.getView(), 1, 0);
    }

    private void addCountChartsGUI()
    {
        countChartBox = new HBox();

        animalCountDisplay = new AnimalCountDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getAnimalStatsTracker(), rightMapStatsTracker.getAnimalStatsTracker());
        newEraGUIObservers.add(animalCountDisplay);

        countChartBox.getChildren().add(animalCountDisplay.getChart());

        plantCountDisplay = new PlantCountDisplay(app, controlPanelWidth,
                leftMapStatsTracker.getPlantCountTracker(), rightMapStatsTracker.getPlantCountTracker());
        newEraGUIObservers.add(plantCountDisplay);

        avgEnergyDisplay = new AvgEnergyDisplay(app, controlPanelWidth, leftMapStatsTracker.getAnimalStatsTracker(),
                rightMapStatsTracker.getAnimalStatsTracker());
        newEraGUIObservers.add(avgEnergyDisplay);

        avgAgeDisplay = new AvgAgeDisplay(app, controlPanelWidth, leftMapStatsTracker.getAnimalStatsTracker(),
                rightMapStatsTracker.getAnimalStatsTracker());
        newEraGUIObservers.add(avgAgeDisplay);

        avgKidsDisplay = new AvgKidsDisplay(app, controlPanelWidth, leftMapStatsTracker.getAnimalStatsTracker(),
                rightMapStatsTracker.getAnimalStatsTracker());
        newEraGUIObservers.add(avgKidsDisplay);

        HBox countChoiceBox = new HBox();
        countChoiceBox.setAlignment(Pos.CENTER);
        countChoiceBox.setPrefWidth(controlPanelWidth);
        countChoiceBox.setPrefHeight(app.getWindowHeight() / 40);

        ToggleGroup countChoiceGroup = new ToggleGroup();
        RadioButton animalRadioButton = new RadioButton("Animals count");
        animalRadioButton.setUserData("animals");
        RadioButton plantRadioButton = new RadioButton("Plants count");
        plantRadioButton.setUserData("plants");
        RadioButton avgEnergyRadioButton = new RadioButton("Average energy");
        avgEnergyRadioButton.setUserData("energy");
        RadioButton avgAgeRadioButton = new RadioButton("Average age");
        avgAgeRadioButton.setUserData("age");
        RadioButton avgKidsRadioButton = new RadioButton("Average kids");
        avgKidsRadioButton.setUserData("kids");

        animalRadioButton.setSelected(true);
        animalRadioButton.setToggleGroup(countChoiceGroup);
        plantRadioButton.setToggleGroup(countChoiceGroup);
        avgEnergyRadioButton.setToggleGroup(countChoiceGroup);
        avgAgeRadioButton.setToggleGroup(countChoiceGroup);
        avgKidsRadioButton.setToggleGroup(countChoiceGroup);

        countChoiceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (countChoiceGroup.getSelectedToggle().getUserData().toString().equals("animals"))
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(animalCountDisplay.getChart());
                }
                else if (countChoiceGroup.getSelectedToggle().getUserData().toString().equals("energy"))
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(avgEnergyDisplay.getChart());
                }
                else if (countChoiceGroup.getSelectedToggle().getUserData().toString().equals("age"))
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(avgAgeDisplay.getChart());
                }
                else if (countChoiceGroup.getSelectedToggle().getUserData().toString().equals("kids"))
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(avgKidsDisplay.getChart());
                }
                else
                {
                    countChartBox.getChildren().clear();
                    countChartBox.getChildren().add(plantCountDisplay.getChart());
                }
            }
        });
        countChoiceBox.getChildren().addAll(animalRadioButton, plantRadioButton, avgEnergyRadioButton,
                avgAgeRadioButton, avgKidsRadioButton);

        statsGrid.add(countChoiceBox, 0, 1, 2, 1);

        statsGrid.add(countChartBox, 0, 2, 2, 1);
    }

    private void addUserDisplay()
    {
        leftUserSetContainer = new VBox();
        rightUserSetContainer = new VBox();
        statsGrid.add(leftUserSetContainer, 0, 3);
        statsGrid.add(rightUserSetContainer, 1, 3);
    }

    public void setUserDisplay(AbstractMap map)
    {
        Platform.runLater(() -> {
            if (map.equals(app.getLeftEngine().getMap()))
            {
                newEraGUIObservers.remove(leftUserSetDisplay);
                leftUserSetDisplay = new UserSetDisplay(app, controlPanelWidth,
                        app.getLeftEngine().getMap().getUserSetAnimalTracker());
                leftUserSetContainer.getChildren().clear();
                leftUserSetContainer.getChildren().add(leftUserSetDisplay.getContainer());
                newEraGUIObservers.add(leftUserSetDisplay);
                app.mapUpdated(app.getLeftEngine().getMap());
            }
            else
            {
                rightUserSetDisplay = new UserSetDisplay(app, controlPanelWidth,
                        app.getRightEngine().getMap().getUserSetAnimalTracker());
                rightUserSetContainer.getChildren().clear();
                rightUserSetContainer.getChildren().add(rightUserSetDisplay.getContainer());
                newEraGUIObservers.add(rightUserSetDisplay);
                app.mapUpdated(app.getRightEngine().getMap());
            }
        });
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
