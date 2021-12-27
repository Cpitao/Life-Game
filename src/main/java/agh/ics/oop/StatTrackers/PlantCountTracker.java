package agh.ics.oop.StatTrackers;

import agh.ics.oop.INewEraObserver;
import agh.ics.oop.INewPlantObserver;
import agh.ics.oop.IPlantEatenObserver;

public class PlantCountTracker extends CountTracker implements INewPlantObserver, IPlantEatenObserver, INewEraObserver {

    public PlantCountTracker()
    {}

    @Override
    public void plantEaten() {
        currentPopulation--;
    }

    @Override
    public void newPlant() {
        currentPopulation++;
    }

}
