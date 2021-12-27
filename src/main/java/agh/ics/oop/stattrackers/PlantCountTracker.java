package agh.ics.oop.stattrackers;

import agh.ics.oop.observers.INewEraObserver;
import agh.ics.oop.observers.INewPlantObserver;
import agh.ics.oop.observers.IPlantEatenObserver;

public class PlantCountTracker extends CountTracker implements INewPlantObserver, IPlantEatenObserver, INewEraObserver {

    public PlantCountTracker()
    {}

    @Override
    public void plantEaten() {
        currentData--;
    }

    @Override
    public void newPlant() {
        currentData++;
    }

}
