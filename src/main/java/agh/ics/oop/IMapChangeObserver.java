package agh.ics.oop;

import agh.ics.oop.maps.AbstractMap;
import javafx.scene.layout.GridPane;

public interface IMapChangeObserver {

    void mapUpdated(AbstractMap map);
}
