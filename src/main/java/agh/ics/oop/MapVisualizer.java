package agh.ics.oop;

import agh.ics.oop.GUI.AnimalMapElement;
import agh.ics.oop.GUI.GUIMapElement;
import agh.ics.oop.guistats.StatsPanel;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.maps.AbstractMap;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MapVisualizer {

    private SimulationEngine engine;
    private StatsPanel statsPanel;
    private GridPane gridPane;
    private AbstractMap map;
    private double cellWidth;
    private double cellHeight;

    public MapVisualizer(SimulationEngine engine, AbstractMap map, GridPane container, StatsPanel statsPanel)
    {
        this.engine = engine;
        this.statsPanel = statsPanel;
        this.map = map;
        this.cellWidth = container.getColumnConstraints().get(0).getPrefWidth() / (map.getWidth() + 1);
        this.cellHeight = container.getRowConstraints().get(0).getPrefHeight() / (map.getHeight() + 1);
        gridPane = generateMapGrid();
    }

    public GridPane generateMapGrid()
    {
        GridPane grid = new GridPane();

        for (int i=0; i < map.getWidth() + 1; i++)
        {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0/map.getWidth());
            columnConstraints.setMinWidth(0);
            columnConstraints.setMaxWidth(cellWidth);
            grid.getColumnConstraints().add(columnConstraints);
        }
        for (int i=0; i < map.getHeight() + 1; i++)
        {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0/map.getHeight());
            rowConstraints.setMinHeight(0);
            rowConstraints.setMaxHeight(cellHeight);
            grid.getRowConstraints().add(rowConstraints);
        }
        for (int i=1; i < map.getWidth() + 1; i++)
        {
            Label newLabel = new Label(Integer.toString(i-1));
            newLabel.setFont(new Font(10));
            grid.add(newLabel, i, 0);
            GridPane.setFillWidth(newLabel, true);
            GridPane.setHalignment(newLabel, HPos.CENTER);
        }

        for (int i=1; i < map.getHeight() + 1; i++)
        {
            Label newLabel = new Label(Integer.toString(i-1));
            newLabel.setFont(new Font(10));
            GridPane.setFillWidth(newLabel, true);
            GridPane.setHalignment(newLabel, HPos.CENTER);
            grid.add(newLabel, 0, map.getHeight() - i + 1);
        }

        return grid;
    }

    public void drawMapElements()
    {
        Set<Vector2d> nonEmptyPositions = new HashSet<>();
        try { // exception may occur if delay is too short, can be ignored
            nonEmptyPositions.addAll(map.getAnimals().keySet());
            nonEmptyPositions.addAll(map.getPlants().keySet());
        }
        catch (ConcurrentModificationException e) {}
        for (Vector2d position: nonEmptyPositions)
        {
            Rectangle objectRectangle;
            if (map.objectAt(position) instanceof Animal)
            {
                AnimalMapElement animalMapElement = new AnimalMapElement(engine, map.objectAt(position), statsPanel);
                objectRectangle = animalMapElement.getRectangle();
            }
            else
            {
                GUIMapElement guiMapElement = new GUIMapElement(map.objectAt(position));
                objectRectangle = guiMapElement.getRectangle();
            }
            objectRectangle.setHeight(cellHeight);
            objectRectangle.setWidth(cellWidth);
            this.gridPane.add(objectRectangle, position.x + 1,
                    map.getHeight() - position.y, 1, 1);
        }

        // show tracked animal
        if (map.getUserSetAnimalTracker() != null && map.getUserSetAnimalTracker().getDeathEra() == 0)
        {
            Rectangle trackedRectangle = new Rectangle(cellWidth, cellHeight);
            trackedRectangle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 2;");
            this.gridPane.add(trackedRectangle,
                    map.getUserSetAnimalTracker().getTrackedAnimal().getPosition().x + 1,
                    map.getHeight() - map.getUserSetAnimalTracker().getTrackedAnimal().getPosition().y);
        }
    }

    public GridPane getHighlightedGrid(GridPane grid, LinkedList<Vector2d> positions)
    {
        for (Vector2d position: positions)
        {
            Rectangle newRectangle = new Rectangle(cellWidth, cellHeight);
            newRectangle.setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 2;");
            grid.add(newRectangle, position.x + 1, map.getHeight() - position.y);
        }

        return grid;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
