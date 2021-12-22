package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.AbstractMap;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;

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
        gridPane.setGridLinesVisible(true);
    }

    public GridPane generateMapGrid()
    {
        GridPane grid = new GridPane();

        for (int i=0; i < map.getWidth() + 1; i++)
        {
            grid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int i=0; i < map.getHeight() + 1; i++)
        {
            grid.getRowConstraints().add(new RowConstraints(cellHeight));
        }

        for (int i=1; i < map.getWidth() + 1; i++)
        {
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            grid.add(new Label(Integer.toString(i-1)), 0, i, 1, 1);
        }

        for (int i=1; i < map.getHeight() + 1; i++)
        {
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            grid.add(new Label(Integer.toString(i-1)), i, 0, 1, 1);
        }
        grid.setGridLinesVisible(true);

        return grid;
    }

    public void drawMapElements()
    {
        for (Vector2d position: map.getAnimals().keySet())
        {
            Animal animal = map.getAnimals().get(position).last();
            Rectangle newRectangle = new Rectangle(cellWidth, cellHeight);
            newRectangle.setFill(animal.getEnergyLevel().toColor());
            this.gridPane.add(newRectangle, position.x + 1, position.y + 1, 1, 1);
        }

        for (Vector2d position: map.getPlants().keySet())
        {
            if (map.getAnimals().get(position) == null)
            {
                Plant plant = map.getPlants().get(position);
                GuiPlantBox plantBox = new GuiPlantBox(plant);
                this.gridPane.add(plantBox.getVbox(), position.x, position.y, 1, 1);
            }
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
