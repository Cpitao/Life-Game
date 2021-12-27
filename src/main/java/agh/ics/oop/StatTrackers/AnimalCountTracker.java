package agh.ics.oop.StatTrackers;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.IMapChangeObserver;
import agh.ics.oop.INewAnimalObserver;
import agh.ics.oop.INewEraObserver;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.AbstractMap;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public class AnimalCountTracker extends CountTracker implements INewAnimalObserver, IAnimalDeathObserver {

    public AnimalCountTracker()
    {}

    @Override
    public void animalDied(Animal animal) {
        currentPopulation--;
    }

    @Override
    public void newAnimalPlaced(Animal animal) {
        currentPopulation++;
    }

}
