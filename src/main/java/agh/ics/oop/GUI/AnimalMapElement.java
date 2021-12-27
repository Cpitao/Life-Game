package agh.ics.oop.GUI;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.guistats.StatsPanel;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;
import agh.ics.oop.maps.AbstractMap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AnimalMapElement extends GUIMapElement{

    private StatsPanel statsPanel;
    public AnimalMapElement(SimulationEngine engine, IMapElement mapElement, StatsPanel statsPanel) {
        super(mapElement);
        this.statsPanel = statsPanel;
        this.rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!engine.isRunning()) {
                    engine.getMap().unsetUserSetAnimalTracker();
                    engine.getMap().setUserSetAnimalTracker((Animal) mapElement);
                    statsPanel.setUserDisplay(engine.getMap());
                }
            }
        });
    }


}
