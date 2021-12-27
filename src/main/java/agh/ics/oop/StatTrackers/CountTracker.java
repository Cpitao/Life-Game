package agh.ics.oop.StatTrackers;

import agh.ics.oop.INewEraObserver;
import javafx.scene.chart.XYChart;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public abstract class CountTracker implements INewEraObserver {

    protected LinkedList<Number> historicalPopulation = new LinkedList<>();
    protected int currentPopulation = 0;
    protected int currentEra = 0;

    public CountTracker(){}

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

    public XYChart.Data<Number, Number> getLastPoint()
    {
        return new XYChart.Data<Number, Number>(currentEra, historicalPopulation.getLast());
    }

    @Override
    public void newEra() {
        historicalPopulation.add(currentPopulation);
        currentEra++;
        if (historicalPopulation.size() > 100)
            historicalPopulation.remove(0);
    }
}
