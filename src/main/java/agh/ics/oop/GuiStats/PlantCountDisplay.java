package agh.ics.oop.GuiStats;

import agh.ics.oop.App;
import agh.ics.oop.StatTrackers.CountTracker;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class PlantCountDisplay extends AbstractCountDisplay{

    public PlantCountDisplay(App app, double controlPanelWidth,
                             CountTracker leftCountTracker, CountTracker rightCountTracker) {
        super(app, controlPanelWidth, leftCountTracker, rightCountTracker);
    }

    @Override
    public void init() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Era");
        yAxis = new NumberAxis();
        yAxis.setLabel("Plants population");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Plants population over time");
        lineChart.setPrefWidth(controlPanelWidth);

        leftSeries.setName("Left map");
        rightSeries.setName("Right map");

        lineChart.getData().addAll(leftSeries, rightSeries);
        lineChart.setAnimated(false);
    }

}
