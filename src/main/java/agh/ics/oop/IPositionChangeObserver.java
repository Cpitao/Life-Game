package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;

public interface IPositionChangeObserver {

    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);

}
