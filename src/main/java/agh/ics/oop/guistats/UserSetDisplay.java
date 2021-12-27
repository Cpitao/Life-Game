package agh.ics.oop.guistats;

import agh.ics.oop.App;
import agh.ics.oop.UserSetAnimalTracker;
import agh.ics.oop.observers.IGUIChangeObserver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserSetDisplay implements IGUIChangeObserver {

    private final UserSetAnimalTracker tracker;
    private final VBox container = new VBox();

    private final Label animalGenomeLabel;
    private final Label childrenCountLabel;
    private final Label descendantsCountLabel;
    private final Label diedAtLabel;

    public UserSetDisplay(App app, double controlPanelWidth, UserSetAnimalTracker tracker)
    {
        this.tracker = tracker;
        this.animalGenomeLabel = new Label("Animal genome:\n" + tracker.getAnimalGenes().toString());
        animalGenomeLabel.setPrefWidth(controlPanelWidth / 2);
        this.childrenCountLabel = new Label("Children count:\n" + tracker.getChildrenCount());
        childrenCountLabel.setPrefWidth(controlPanelWidth / 2);
        this.descendantsCountLabel = new Label("Descendants count:\n" + tracker.getDescendantsCount());
        descendantsCountLabel.setPrefWidth(controlPanelWidth / 2);
        if (tracker.getDeathEra() == 0)
            this.diedAtLabel = new Label("Died at:\n-");
        else
            this.diedAtLabel = new Label("Died at:\n" + tracker.getDeathEra());
        diedAtLabel.setPrefWidth(controlPanelWidth / 2);

        container.getChildren().addAll(animalGenomeLabel, childrenCountLabel, descendantsCountLabel, diedAtLabel);
        container.setPadding(new Insets(10));
    }

    @Override
    public void updateGUI() {
        Platform.runLater(() -> {
            animalGenomeLabel.setText("Animal genome:\n" + tracker.getAnimalGenes().toString());
            childrenCountLabel.setText("Children count:\n" + tracker.getChildrenCount());
            descendantsCountLabel.setText("Descendants count:\n" + tracker.getDescendantsCount());
            if (tracker.getDeathEra() != 0)
                diedAtLabel.setText("Died at:\n" + tracker.getDeathEra());
        });
    }

    public VBox getContainer()
    {
        return container;
    }
}
