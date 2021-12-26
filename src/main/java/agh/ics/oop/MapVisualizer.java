package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.AbstractMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class MapVisualizer {

    private GridPane gridPane;
    private AbstractMap map;
    private double cellWidth;
    private double cellHeight;

    public MapVisualizer(AbstractMap map, GridPane container)
    {
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
            Rectangle objectRectangle = new Rectangle(cellWidth, cellHeight);
            objectRectangle.setFill(map.objectAt(position).toColor());
            this.gridPane.add(objectRectangle, position.x + 1,
                    map.getHeight() - position.y, 1, 1);
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
