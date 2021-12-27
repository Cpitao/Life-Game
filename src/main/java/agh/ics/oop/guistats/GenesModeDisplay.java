package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.observers.IGUIChangeObserver;
import agh.ics.oop.stattrackers.GenesStatsTracker;
import agh.ics.oop.mapparts.Genes;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class GenesModeDisplay implements IGUIChangeObserver {

    private App app;
    private SimulationEngine engine;
    private double controlPanelWidth;
    private GenesStatsTracker genesStatsTracker;

    private ScrollPane scrollPane;

    private VBox genesModesLabels;

    public GenesModeDisplay(App app, double controlPanelWidth, GenesStatsTracker genesStatsTracker,
                            SimulationEngine engine)
    {
        this.app = app;
        this.engine = engine;
        this.controlPanelWidth = controlPanelWidth;
        this.genesStatsTracker = genesStatsTracker;
        scrollPane = init();
    }

    public ScrollPane init()
    {
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(controlPanelWidth / 2);
        scrollPane.setMaxHeight(app.getWindowHeight() / 40);
        scrollPane.setMaxHeight(app.getWindowHeight() / 40);
        genesModesLabels = new VBox();
        Label genesCaptionLabel = new Label("Genotype mode and count\nClick to view\n");
        genesCaptionLabel.setAlignment(Pos.CENTER);
        genesModesLabels.getChildren().add(genesCaptionLabel);

        scrollPane.setContent(genesModesLabels);

        return scrollPane;
    }

    @Override
    public void updateGUI() {
        Platform.runLater(() -> {
            LinkedList<Genes> genesModes = genesStatsTracker.getGenesMode();
            int genesCount = genesStatsTracker.getGenesModeCount();
            genesModesLabels.getChildren().clear();
            genesModesLabels.getChildren().add(new Label("Genotype mode and count\nClick to view\n" + genesCount));
            for (Genes genes: genesModes)
            {
                Label genesLabel = new Label(genes.toString());
                genesLabel.setAlignment(Pos.CENTER);
                genesModesLabels.getChildren().add(genesLabel);
            }
        });
    }

    public ScrollPane getView()
    {
        return scrollPane;
    }


}
