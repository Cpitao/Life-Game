package agh.ics.oop.StatTrackers;

import agh.ics.oop.*;
import agh.ics.oop.GuiStats.StatsPanel;
import agh.ics.oop.mapparts.Animal;

import java.util.LinkedList;

public class MapStatsTracker {

    private StatsPanel GUIObserver;

    private GenesStatsTracker genesStatsTracker = new GenesStatsTracker();
    private AnimalCountTracker animalCountTracker = new AnimalCountTracker();
    private PlantCountTracker plantCountTracker = new PlantCountTracker();

    private LinkedList<INewAnimalObserver> newAnimalObservers = new LinkedList<>();
    private LinkedList<IAnimalDeathObserver> animalDeathObservers = new LinkedList<>();
    private LinkedList<INewPlantObserver> newPlantObservers = new LinkedList<>();
    private LinkedList<IPlantEatenObserver> plantEatenObservers = new LinkedList<>();
    private LinkedList<INewEraObserver> newEraObservers = new LinkedList<>();

    public MapStatsTracker()
    {
        this.newAnimalObservers.add(genesStatsTracker);
        this.animalDeathObservers.add(genesStatsTracker);

        this.newAnimalObservers.add(animalCountTracker);
        this.animalDeathObservers.add(animalCountTracker);
        this.newEraObservers.add(animalCountTracker);

        this.plantEatenObservers.add(plantCountTracker);
        this.newPlantObservers.add(plantCountTracker);
        this.newEraObservers.add(plantCountTracker);
    }

    public void newAnimal(Animal animal)
    {
        for (INewAnimalObserver observer: newAnimalObservers)
        {
            observer.newAnimalPlaced(animal);
        }
    }

    public void animalDied(Animal animal)
    {
        for (IAnimalDeathObserver observer: animalDeathObservers)
        {
            observer.animalDied(animal);
        }
    }

    public void newPlant()
    {
        for (INewPlantObserver observer: newPlantObservers)
            observer.newPlant();
    }

    public void plantEaten()
    {
        for (IPlantEatenObserver observer: plantEatenObservers)
            observer.plantEaten();
    }

    public void newEra()
    {
        for (INewEraObserver observer: newEraObservers)
        {
            observer.newEra();
        }

        GUIObserver.newEraGUIUpdates();
    }

    public GenesStatsTracker getGenesStatsTracker() {
        return genesStatsTracker;
    }

    public AnimalCountTracker getAnimalCountTracker()
    {
        return animalCountTracker;
    }

    public PlantCountTracker getPlantCountTracker()
    {
        return plantCountTracker;
    }

    public void assignGUIPanel(StatsPanel statsPanel)
    {
        this.GUIObserver = statsPanel;
    }

}
