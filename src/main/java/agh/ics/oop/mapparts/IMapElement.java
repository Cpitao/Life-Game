package agh.ics.oop.mapparts;

import agh.ics.oop.Vector2d;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedList;

public interface IMapElement {

    Vector2d getPosition();
    String toString();
    Color toColor();
}
