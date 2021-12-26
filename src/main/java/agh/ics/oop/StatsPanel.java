package agh.ics.oop;

import agh.ics.oop.StatTrackers.MapStatsTracker;
import agh.ics.oop.mapparts.Genes;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class StatsPanel {

    private App app;
    private double controlPanelWidth;
    private MapStatsTracker leftMapStatsTracker;
    private MapStatsTracker rightMapStatsTracker;

    private GridPane statsGrid;

    private ScrollPane leftGenesModesDisplay;
    private ScrollPane rightGenesModesDisplay;

    public StatsPanel(App app, MapStatsTracker leftMapStatsTracker,
                      MapStatsTracker rightMapStatsTracker, double controlPanelWidth)
    {
        this.app = app;
        this.leftMapStatsTracker = leftMapStatsTracker;
        this.rightMapStatsTracker = rightMapStatsTracker;

        leftMapStatsTracker.assignGUIPanel(this);
        rightMapStatsTracker.assignGUIPanel(this);

        this.controlPanelWidth = controlPanelWidth;
        this.updateGrid();
    }

    public void updateGrid()
    {
        statsGrid = new GridPane();
        statsGrid.setGridLinesVisible(true);
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));
        statsGrid.getColumnConstraints().add(new ColumnConstraints(controlPanelWidth / 2));

        leftGenesModesDisplay = getGenesModesDisplay(leftMapStatsTracker.getGenesStatsTracker().getGenesMode(),
                leftMapStatsTracker.getGenesStatsTracker().getGenesModeCount());
        rightGenesModesDisplay = getGenesModesDisplay(rightMapStatsTracker.getGenesStatsTracker().getGenesMode(),
                rightMapStatsTracker.getGenesStatsTracker().getGenesModeCount());

        statsGrid.add(leftGenesModesDisplay, 0, 0);
        statsGrid.add(rightGenesModesDisplay, 1, 0);

        XYChart.Series<Number, Number> leftSeries = leftMapStatsTracker.getAnimalCountTracker().getSeries();
        XYChart.Series<Number, Number> rightSeries = rightMapStatsTracker.getAnimalCountTracker().getSeries();

        LineChart<Number, Number> animalCountChart = getAnimalCountChart(leftSeries, rightSeries);
        statsGrid.add(animalCountChart, 0, 1, 2, 1);
    }

    public ScrollPane getGenesModesDisplay(LinkedList<Genes> genesModes, int genesCount)
    {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(controlPanelWidth / 2);
        scrollPane.setMaxHeight(app.getWindowHeight() / 40);
        scrollPane.setMaxHeight(app.getWindowHeight() / 40);
        VBox genesModesLabels = new VBox();
        Label genesCaptionLabel = new Label("Genotype mode and count");
        genesCaptionLabel.setAlignment(Pos.CENTER);
        genesModesLabels.getChildren().add(genesCaptionLabel);
        for (Genes genes: genesModes)
        {
            Label genesLabel = new Label(genes.toString() + " count " + genesCount);
            genesLabel.setAlignment(Pos.CENTER);
            genesModesLabels.getChildren().add(genesLabel);
        }

        scrollPane.setContent(genesModesLabels);

        return scrollPane;
    }

    public LineChart<Number, Number> getAnimalCountChart(XYChart.Series<Number, Number> leftSeries,
                                                         XYChart.Series<Number, Number> rightSeries)
    {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Era");
        if (leftSeries.getData().size() > 0 && rightSeries.getData().size() > 0) {
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(Math.min((int) leftSeries.getData().get(0).getXValue(),
                    (int) rightSeries.getData().get(0).getXValue()) - 50);
            xAxis.setUpperBound(Math.max((int) leftSeries.getData().get(0).getXValue(),
                    (int) rightSeries.getData().get(0).getXValue()) + 150);
        }
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Animals population");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Animal population over time");
        lineChart.setPrefWidth(controlPanelWidth);

        leftSeries.setName("Left map");
        rightSeries.setName("Right map");

        lineChart.getData().addAll(leftSeries, rightSeries);
        lineChart.setAnimated(false);


        return lineChart;
    }

    public void refreshGUI()
    {
        app.statsUpdated();
    }

    public GridPane getStatsGrid()
    {
        this.updateGrid();
        return statsGrid;
    }


}
