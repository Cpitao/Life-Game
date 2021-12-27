package agh.ics.oop.mapparts;

import javafx.scene.paint.Color;

public interface IMapElement {

    Vector2d getPosition();
    String toString();
    Color toColor();
}
