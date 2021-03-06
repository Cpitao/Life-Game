package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.stattrackers.AnimalStatsTracker;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class AnimalCountDisplay extends AbstractCountDisplay {

    public AnimalCountDisplay(App app, double controlPanelWidth,
                              AnimalStatsTracker leftCountTracker, AnimalStatsTracker rightCountTracker) {
        super(app, controlPanelWidth, leftCountTracker, rightCountTracker);
    }

    @Override
    public void init()
    {
        xAxis = new NumberAxis();
        xAxis.setLabel("Era");
        yAxis = new NumberAxis();
        yAxis.setLabel("Animals population");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Animal population over time");
        lineChart.setPrefWidth(controlPanelWidth);

        leftSeries.setName("Left map");
        rightSeries.setName("Right map");

        lineChart.getData().addAll(leftSeries, rightSeries);
        lineChart.setAnimated(false);
    }

}
