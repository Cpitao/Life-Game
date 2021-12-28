package agh.ics.oop.stattrackers;

import agh.ics.oop.observers.IAnimalDeathObserver;
import agh.ics.oop.observers.IAnimalEnergyChangeObserver;
import agh.ics.oop.observers.IChildrenCountObserver;
import agh.ics.oop.observers.INewAnimalObserver;
import agh.ics.oop.mapparts.Animal;
import javafx.scene.chart.XYChart;

import java.util.LinkedList;

public class AnimalStatsTracker extends CountTracker implements INewAnimalObserver, IAnimalDeathObserver,
        IAnimalEnergyChangeObserver, IChildrenCountObserver {



    private LinkedList<Number> historicalAvgEnergy = new LinkedList<>();
    private LinkedList<Number> historicalAvgAge = new LinkedList<>();
    private int totalEnergy = 0;
    private int totalAge = 0;
    private int animalsDied = 0;
    private int totalKids = 0;

    public AnimalStatsTracker()
    {}

    @Override
    public void animalDied(Animal animal) {
        currentData--;
        totalEnergy -= animal.getEnergy(); // if animal had negative energy, this will change its contribution to 0
        totalAge += animal.getAge();
        animalsDied++;
        totalKids -= animal.getKids();
    }

    @Override
    public void newAnimalPlaced(Animal animal) {
        currentData++;
        totalEnergy += animal.getEnergy();
    }

    @Override
    public void energyChanged(int energyChange) {
        totalEnergy += energyChange;
    }

    @Override
    public void newEra()
    {

        historicalAvgEnergy.add(getAvgEnergy());
        historicalAvgAge.add(getAvgAge());
        super.newEra();
        if (historicalAvgEnergy.size() > 2000)
            historicalAvgEnergy.remove(0);
        if (historicalAvgAge.size() > 2000)
            historicalAvgAge.remove(0);
    }

    public double getAvgEnergy()
    {
        if (currentData > 0)
            return (double) totalEnergy / currentData;
        return 0;
    }

    public XYChart.Data<Number, Number> getAvgEnergyLastPoint()
    {
        return new XYChart.Data<>(currentEra, getAvgEnergy());
    }

    public double getAvgAge()
    {
        if (animalsDied > 0)
            return (double) totalAge / animalsDied;
        return 0;
    }

    public XYChart.Data<Number, Number> getAvgAgeLastPoint()
    {
        return new XYChart.Data<>(currentEra, getAvgAge());
    }

    @Override
    public void newKid(Animal animal) {
        totalKids++;
    }

    public double getAvgKids()
    {
        if (currentData > 0)
            return (double) totalKids / currentData;
        return 0;
    }

    public LinkedList<Number> getHistoricalAvgEnergy() {
        return historicalAvgEnergy;
    }

    public LinkedList<Number> getHistoricalAvgAge() {
        return historicalAvgAge;
    }

    public XYChart.Data<Number, Number> getAvgKidsLastPoint()
    {
        return new XYChart.Data<>(currentEra, getAvgKids());
    }
}
