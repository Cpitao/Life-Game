package agh.ics.oop.observers;

import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.mapparts.Animal;

public interface IPositionChangeObserver {

    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);

}
