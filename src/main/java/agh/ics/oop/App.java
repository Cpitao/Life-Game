package agh.ics.oop;

import agh.ics.oop.GUI.InitialUserSetup;
import agh.ics.oop.GUI.MapVisualizer;
import agh.ics.oop.guistats.ControlPanel;
import agh.ics.oop.guistats.StatsPanel;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.Genes;
import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.observers.IMapChangeObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class App extends Application implements IMapChangeObserver {

    private double windowWidth;
    private double windowHeight;
    private double controlPanelWidth;

    private Stage primaryStage;
    private Scene currentScene;

    private HBox container;
    private StatsPanel statsPanel;
    private VBox middlePanel;
    private GridPane controlPanelGrid;
    private GridPane statsPanelGrid;

    private GridPane leftMapContainer;
    private GridPane rightMapContainer;

    private GridPane leftMapGrid;
    private GridPane rightMapGrid;

    private Thread leftEngineThread;
    private Thread rightEngineThread;

    private SimulationEngine leftEngine;
    private SimulationEngine rightEngine;

    private AbstractMap leftMap;
    private AbstractMap rightMap;

    @Override
    public void init()
    {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setMaximized(true);
        Scene scene = new Scene(new Button());
        this.primaryStage.setScene(scene); // put empty scene on maximized stage to get width/height
        this.primaryStage.show();
        this.windowHeight = scene.getHeight() - 10;
        this.windowWidth = scene.getWidth() - 10;
        this.controlPanelWidth = windowWidth / 3;
        InitialUserSetup userSetup = new InitialUserSetup(this, windowWidth, windowHeight);
        currentScene = userSetup.getInitialScene();
        this.primaryStage.setScene(currentScene);
    }

    @Override
    public void mapUpdated(AbstractMap map) {
        if (map == leftMap) {
            Platform.runLater(() ->
            {
                MapVisualizer mapVisualizer = new MapVisualizer(leftEngine, map, leftMapContainer, statsPanel);
                mapVisualizer.drawMapElements();
                leftMapGrid = mapVisualizer.getGridPane();
                leftMapContainer.getChildren().clear();
                leftMapContainer.getChildren().add(leftMapGrid);
                currentScene.setRoot(container);
                primaryStage.setScene(currentScene);
            });
        }
        else if (map == rightMap){
            Platform.runLater(() ->
            {
                MapVisualizer mapVisualizer = new MapVisualizer(rightEngine, map, rightMapContainer, statsPanel);
                mapVisualizer.drawMapElements();
                rightMapGrid = mapVisualizer.getGridPane();
                rightMapContainer.getChildren().clear();
                rightMapContainer.getChildren().add(rightMapGrid);
                currentScene.setRoot(container);
                primaryStage.setScene(currentScene);
            });
        }
    }

    public void mapsInitiated(AbstractMap leftMap, AbstractMap rightMap, int leftMapAnimalCount,
                              int rightMapAnimalCount)
    {
        this.leftMap = leftMap;
        this.rightMap = rightMap;

        leftEngine = new SimulationEngine(leftMap, this);
        leftEngineThread = new Thread(leftEngine);
        rightEngine = new SimulationEngine(rightMap, this);
        rightEngineThread = new Thread(rightEngine);


        leftMapContainer = new GridPane();
        leftMapContainer.setPrefWidth(windowWidth / 3);
        leftMapContainer.setMaxWidth(windowWidth / 3);
        leftMapContainer.getRowConstraints().add(new RowConstraints(windowHeight - 20));
        leftMapContainer.getColumnConstraints().add(new ColumnConstraints(windowWidth / 3));
        MapVisualizer leftMapVisualizer = new MapVisualizer(leftEngine, leftMap, leftMapContainer, statsPanel);
        leftMapVisualizer.drawMapElements();
        leftMapGrid = leftMapVisualizer.getGridPane();
        GridPane.setFillWidth(leftMapGrid, true);
        leftMapContainer.getChildren().add(leftMapGrid);

        rightMapContainer = new GridPane();
        rightMapContainer.setPrefWidth(windowWidth / 3);
        rightMapContainer.getRowConstraints().add(new RowConstraints(windowHeight - 20));
        rightMapContainer.getColumnConstraints().add(new ColumnConstraints(windowWidth / 3));
        MapVisualizer rightMapVisualizer = new MapVisualizer(rightEngine, rightMap, rightMapContainer, statsPanel);
        rightMapVisualizer.drawMapElements();
        rightMapGrid = rightMapVisualizer.getGridPane();
        GridPane.setFillWidth(rightMapGrid, true);
        rightMapContainer.getChildren().add(rightMapGrid);

        setMapsScene(leftMapAnimalCount, rightMapAnimalCount);
    }

    private void setMapsScene(int leftAnimalCount, int rightAnimalCount)
    {
        container = new HBox();
        container.setPadding(new Insets(5));

        middlePanel = new VBox();
        ControlPanel controlPanel = new ControlPanel(this, controlPanelWidth);
        statsPanel = new StatsPanel(this,
                leftMap.getMapStatisticsTracker(), rightMap.getMapStatisticsTracker(), controlPanelWidth);
        controlPanelGrid = controlPanel.getLayoutGrid();
        statsPanelGrid = statsPanel.getStatsGrid();

        HBox saveButtons = getSaveButtons();

        middlePanel.getChildren().addAll(controlPanelGrid, statsPanelGrid, saveButtons);

        container.getChildren().addAll(leftMapContainer, middlePanel, rightMapContainer);

        this.currentScene = new Scene(container, windowWidth, windowHeight);
        primaryStage.setScene(currentScene);

        for (int i=0; i < leftAnimalCount; i++)
            leftMap.placeAnimal(new Animal(leftMap));
        for (int i=0; i < rightAnimalCount; i++)
            rightMap.placeAnimal(new Animal(rightMap));

        mapUpdated(leftMap);
        mapUpdated(rightMap);

        leftEngineThread.start();
        rightEngineThread.start();
    }

    public HBox getSaveButtons()
    {
        GridPane gridPane = new GridPane();
        Button leftSave = new Button("Save map info");
        leftSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (leftEngine.isRunning())
                    return;
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile == null)
                    return;
                try {
                    leftMap.getMapStatisticsTracker().saveToCSV(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button rightSave = new Button("Save map info");
        rightSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rightEngine.isRunning())
                    return;
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile == null)
                    return;
                try {
                    rightMap.getMapStatisticsTracker().saveToCSV(selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        gridPane.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));
        gridPane.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));

        gridPane.add(leftSave, 0, 0);
        gridPane.add(rightSave, 1, 0);

        HBox buttonsContainer = new HBox();
        buttonsContainer.getChildren().add(gridPane);
        return buttonsContainer;
    }
    public void showModeGeneAnimals(SimulationEngine engine, GridPane mapGrid)
    {
        Platform.runLater(() ->
        {
            Genes mode = engine.getMap().getMapStatisticsTracker().getGenesStatsTracker().getGenesMode().get(0);
            LinkedList<Vector2d> positions = new LinkedList<>();
            for (LinkedList<Animal> animals: engine.getMap().getAnimals().values())
            {
                for (Animal animal: animals)
                {
                    if (animal.getGenes().equals(mode))
                        positions.add(animal.getPosition());
                }
            }
            GridPane newMapGrid = new MapVisualizer(engine, engine.getMap(),
                    leftMapContainer, statsPanel).getHighlightedGrid(mapGrid, positions);
            if (engine.equals(leftEngine)) {
                leftMapContainer.getChildren().clear();
                leftMapContainer.getChildren().add(newMapGrid);
            }
            else
            {
                rightMapContainer.getChildren().clear();
                rightMapContainer.getChildren().add(newMapGrid);
            }
        });

    }

    public SimulationEngine getLeftEngine() {
        return leftEngine;
    }

    public SimulationEngine getRightEngine() {
        return rightEngine;
    }

    public double getWindowHeight()
    {
        return windowHeight;
    }

    public GridPane getLeftMapGrid()
    {
        return leftMapGrid;
    }

    public GridPane getRightMapGrid()
    {
        return rightMapGrid;
    }
}
