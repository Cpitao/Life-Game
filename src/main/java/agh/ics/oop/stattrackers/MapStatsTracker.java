package agh.ics.oop.stattrackers;

import agh.ics.oop.guistats.StatsPanel;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.observers.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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
        out.add("\"Animal counts\",\"Plant counts\",\"Average energy levels\",\"Average life length\"");
        LinkedList<Number>[] input = new LinkedList[4];
        input[0] = getAnimalStatsTracker().getHistoricalData();
        input[1] = getPlantCountTracker().getHistoricalData();
        input[2] = getAnimalStatsTracker().getHistoricalAvgEnergy();
        input[3] = getAnimalStatsTracker().getHistoricalAvgAge();
        out.addAll(Arrays.stream(toCSV(input)).toList());
        Number[] averages = new Number[4];
        StringBuilder averagesString = new StringBuilder();
        out.add("averages");
        for (int i=0; i < 3; i++)
        {
            averages[i] = getAverage(input[i]);
            averagesString.append(averages[i].toString() + ",");
        }
        averages[3] = getAverage(input[3]);
        averagesString.append(averages[3].toString());
        out.add(averagesString.toString());
        File csvOut = new File(outFile);
        try (PrintWriter pw = new PrintWriter(csvOut))
        {
            out.stream()
                    .forEach(pw::println);
        }
    }

    private String[] toCSV(LinkedList<Number>[] numbersArr) {
        String[] result = new String[numbersArr[0].size()];
        int k = 0;
        for (Number number: numbersArr[0])
        {
            result[k] = number.toString();
            k++;
        }
        for (int i=1; i < numbersArr.length; i++) {
            for (int j=0; j < numbersArr[i].size(); j++)
            {
                result[j] += "," + numbersArr[i].get(j);
            }
        }

        return result;
    }

    private Number getAverage(LinkedList<Number> numbers)
    {
        int sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }
        return sum / numbers.size();
    }
}
