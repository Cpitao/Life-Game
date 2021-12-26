package agh.ics.oop.StatTrackers;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.INewAnimalObserver;
import agh.ics.oop.StatsPanel;
import agh.ics.oop.mapparts.Animal;

import java.util.LinkedList;

public class MapStatsTracker {

    private StatsPanel GUIObserver;

    private GenesStatsTracker genesStatsTracker = new GenesStatsTracker();
    private AnimalCountTracker animalCountTracker = new AnimalCountTracker();
    private PlantCountTracker plantCountTracker = new PlantCountTracker();

    private LinkedList<INewAnimalObserver> newAnimalObservers = new LinkedList<>();
    private LinkedList<IAnimalDeathObserver> animalDeathObservers = new LinkedList<>();


    public MapStatsTracker()
    {
        this.newAnimalObservers.add(genesStatsTracker);
        this.animalDeathObservers.add(genesStatsTracker);

        this.newAnimalObservers.add(animalCountTracker);
        this.animalDeathObservers.add(animalCountTracker);
    }

    public void newAnimal(Animal animal)
    {
        for (INewAnimalObserver observer: newAnimalObservers)
        {
            observer.newAnimalPlaced(animal);
        }

        GUIObserver.refreshGUI();
    }

    public void animalDied(Animal animal)
    {
        for (IAnimalDeathObserver observer: animalDeathObservers)
        {
            observer.animalDied(animal);
        }
        GUIObserver.refreshGUI();
    }

    public void newPlant()
    {
        plantCountTracker.newPlant();
    }

    public void plantEaten()
    {
        plantCountTracker.plantEaten();
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
