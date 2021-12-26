package agh.ics.oop;

import agh.ics.oop.mapparts.IMapElement;
import agh.ics.oop.maps.AbstractMap;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Plant implements IMapElement {

    private static Color plantColor = Color.rgb(0, 255, 0);
    private final Vector2d position;
    private final AbstractMap map;
    private LinkedList<IMapElementRemovedObserver> objectRemovedObservers = new LinkedList<>();

    public Plant(AbstractMap map, Vector2d position)
    {
        this.position = position;
        this.map = map;
        objectRemovedObservers.add(map);
    }

    @Override
    public Vector2d getPosition()
    {
        return this.position;
    }

    public Color getPlantColor()
    {
        return plantColor;
    }

    public void notifyPlantEaten()
    {
        for (IMapElementRemovedObserver observer: objectRemovedObservers)
        {
            observer.mapElementRemoved(this);
        }
    }

    @Override
    public Color toColor() {
        return Plant.plantColor;
    }
}
