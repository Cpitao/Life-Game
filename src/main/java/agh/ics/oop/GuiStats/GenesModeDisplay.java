package agh.ics.oop.GuiStats;

import agh.ics.oop.App;
import agh.ics.oop.IGUIChangeObserver;
import agh.ics.oop.StatTrackers.GenesStatsTracker;
import agh.ics.oop.mapparts.Genes;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class GenesModeDisplay implements IGUIChangeObserver {

    private App app;
    private double controlPanelWidth;
    private GenesStatsTracker genesStatsTracker;

    private ScrollPane scrollPane;

    private VBox genesModesLabels;

    public GenesModeDisplay(App app, double controlPanelWidth, GenesStatsTracker genesStatsTracker)
    {
        this.app = app;
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
        Label genesCaptionLabel = new Label("Genotype mode and count");
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
            genesModesLabels.getChildren().add(new Label("Genotype mode count " + genesCount));
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
