package agh.ics.oop.observers;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

public interface IMapElementRemovedObserver {

    void mapElementRemoved(IMapElement element);

}
