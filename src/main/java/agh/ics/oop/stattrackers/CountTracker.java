package agh.ics.oop.stattrackers;

import agh.ics.oop.observers.INewEraObserver;
import javafx.scene.chart.XYChart;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

public abstract class CountTracker implements INewEraObserver {



    protected LinkedList<Number> historicalData = new LinkedList<>();
    protected int currentData = 0;
    protected int currentEra = 0;

    public CountTracker(){}

    public XYChart.Series<Number, Number> getSeries()
    {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int i=0;
//        if (currentEra > 2000)
//            i = currentEra - 2000;
        try {
            for (Number number : historicalData) {
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
        return new XYChart.Data<Number, Number>(currentEra, historicalData.getLast());
    }

    @Override
    public void newEra() {
        historicalData.add(currentData);
        currentEra++;
//        if (historicalData.size() > 2000)
//            historicalData.remove(0);
    }

    public LinkedList<Number> getHistoricalData() {
        return historicalData;
    }
}
