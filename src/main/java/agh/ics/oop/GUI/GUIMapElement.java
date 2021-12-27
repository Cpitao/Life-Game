package agh.ics.oop.GUI;

import agh.ics.oop.mapparts.IMapElement;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class GUIMapElement {

    protected IMapElement mapElement;

    protected Rectangle rectangle = new Rectangle();

    public GUIMapElement(IMapElement mapElement)
    {
        this.mapElement = mapElement;

        rectangle.setFill(mapElement.toColor());
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }
}
