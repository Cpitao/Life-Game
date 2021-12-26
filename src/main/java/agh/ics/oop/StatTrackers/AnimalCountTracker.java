package agh.ics.oop.StatTrackers;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.IMapChangeObserver;
import agh.ics.oop.INewAnimalObserver;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.AbstractMap;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public class AnimalCountTracker implements INewAnimalObserver, IAnimalDeathObserver, IMapChangeObserver {

    private LinkedList<Number> historicalPopulation = new LinkedList<>();
    int currentPopulation = 0;
    int currentEra = 0;

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

    public XYChart.Series<Number, Number> getSeries()
    {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int i=0;
        if (currentEra > 100)
            i = currentEra - 100;
        try {
            for (Number number : historicalPopulation) {
                series.getData().add(new XYChart.Data<>(i, number));
                i++;
            }
        }
        catch (ConcurrentModificationException e)
        {}
        return series;
    }

    @Override
    public void mapUpdated(AbstractMap map) {
        historicalPopulation.add(currentPopulation);
        currentEra++;
        if (historicalPopulation.size() > 100)
            historicalPopulation.remove(0);
    }
}
