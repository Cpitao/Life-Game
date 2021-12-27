package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.observers.IGUIChangeObserver;
import agh.ics.oop.stattrackers.CountTracker;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.NoSuchElementException;

public abstract class AbstractCountDisplay implements IGUIChangeObserver {

    protected App app;
    protected double controlPanelWidth;
    protected CountTracker leftCountTracker;
    protected CountTracker rightCountTracker;

    protected NumberAxis xAxis;
    protected NumberAxis yAxis;

    protected XYChart.Series<Number, Number> leftSeries = new XYChart.Series<>();
    protected XYChart.Series<Number, Number> rightSeries = new XYChart.Series<>();
    protected LineChart<Number, Number> lineChart;

    public AbstractCountDisplay(App app, double controlPanelWidth,
                                CountTracker leftCountTracker, CountTracker rightCountTracker)
    {
        this.app = app;
        this.controlPanelWidth = controlPanelWidth;
        this.leftCountTracker = leftCountTracker;
        this.rightCountTracker = rightCountTracker;

        init();
    }

    public abstract void init();

    @Override
    public void updateGUI() {
        Platform.runLater(() -> {
            try { // happens when series is empty
                if (leftSeries.getData().size() > 2000)
                    leftSeries.getData().remove(0);
                leftSeries.getData().add(leftCountTracker.getLastPoint());
            }
            catch (NoSuchElementException ignored){}
            try {
                if (rightSeries.getData().size() > 2000)
                    rightSeries.getData().remove(0);
                rightSeries.getData().add(rightCountTracker.getLastPoint());
            }
            catch (NoSuchElementException ignored){}

//            if (leftSeries.getData().size() > 0 && rightSeries.getData().size() > 0) {
//                xAxis.setAutoRanging(false);
//                xAxis.setLowerBound(Math.min((int) leftSeries.getData().get(0).getXValue(),
//                        (int) rightSeries.getData().get(0).getXValue()) - 50);
//                xAxis.setUpperBound(Math.max((int) leftSeries.getData().get(leftSeries.getData().size()-1).getXValue(),
//                        (int) rightSeries.getData().get(rightSeries.getData().size()-1).getXValue()) + 50);
//            }
        });
    }

    public LineChart<Number, Number> getChart()
    {
        return lineChart;
    }
}
