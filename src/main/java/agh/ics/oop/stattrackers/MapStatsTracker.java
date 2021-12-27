package agh.ics.oop.stattrackers;

import agh.ics.oop.guistats.StatsPanel;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.observers.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapStatsTracker {

    private StatsPanel GUIObserver;

    private GenesStatsTracker genesStatsTracker = new GenesStatsTracker();
    private AnimalStatsTracker animalStatsTracker = new AnimalStatsTracker();
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

        this.newAnimalObservers.add(animalStatsTracker);
        this.animalDeathObservers.add(animalStatsTracker);
        this.newEraObservers.add(animalStatsTracker);

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

    public AnimalStatsTracker getAnimalStatsTracker()
    {
        return animalStatsTracker;
    }

    public PlantCountTracker getPlantCountTracker()
    {
        return plantCountTracker;
    }

    public void assignGUIPanel(StatsPanel statsPanel)
    {
        this.GUIObserver = statsPanel;
    }

    public void saveToCSV(String outFile) throws IOException
    {
        LinkedList<String> out = new LinkedList<>();
        out.add("\"Animal counts\"");
        out.addAll(toCSV(getAnimalStatsTracker().getHistoricalData()));
        out.add("\"Plant counts\"");
        out.addAll(toCSV(getPlantCountTracker().getHistoricalData()));
        out.add("\"Average energy levels\"");
        out.addAll(toCSV(getAnimalStatsTracker().getHistoricalAvgEnergy()));
        out.add("\"Average life length\"");
        out.addAll(toCSV(getAnimalStatsTracker().getHistoricalAvgAge()));

        File csvOut = new File(outFile + "\\out.csv");
        try (PrintWriter pw = new PrintWriter(csvOut))
        {
            out.stream()
                    .forEach(pw::println);
        }
    }

    private LinkedList<String> toCSV(LinkedList<Number> numbers) {
        LinkedList<String> result = new LinkedList<>();
        for (Number number : numbers)
        {
            result.add(number.toString());
        }

        return result;
    }
}
