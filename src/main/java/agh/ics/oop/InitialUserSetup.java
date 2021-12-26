package agh.ics.oop;

import agh.ics.oop.maps.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class InitialUserSetup {

    private final App app;

    private final double containerWidth;
    private final double containerHeight;

    private GridPane leftMapParamsContainer;
    private GridPane rightMapParamsContainer;

    public InitialUserSetup(App app,  double containerWidth, double containerHeight)
    {
        this.app = app;
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
    }

    public Scene getInitialScene()
    {
        VBox outerContainer = new VBox();

        leftMapParamsContainer = getMapParametersContainer();
        rightMapParamsContainer = getMapParametersContainer();

        GridPane paramsContainer = new GridPane();

        Label leftParamsLabel = new Label("Left map parameters");
        leftParamsLabel.setFont(new Font(30));
        Label rightParamsLabel = new Label("Right map parameters");
        rightParamsLabel.setFont(new Font(30));

        paramsContainer.add(leftParamsLabel, 0, 0);
        paramsContainer.add(rightParamsLabel, 1, 0);
        GridPane.setHalignment(leftParamsLabel, HPos.CENTER);
        GridPane.setHalignment(rightParamsLabel, HPos.CENTER);
        leftParamsLabel.setPrefHeight(containerHeight/10);
        rightParamsLabel.setPrefHeight(containerHeight/10);

        paramsContainer.add(leftMapParamsContainer, 0, 1);
        paramsContainer.add(rightMapParamsContainer, 1, 1);
        paramsContainer.setGridLinesVisible(true);

        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefHeight(containerHeight/10);
        confirmButton.setPrefWidth(containerWidth);

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AbstractMap leftMap = createMapWithParams(leftMapParamsContainer, true);
                AbstractMap rightMap = createMapWithParams(rightMapParamsContainer, false);
                app.mapsInitiated(leftMap, rightMap, getAnimalCount(leftMapParamsContainer),
                        getAnimalCount(rightMapParamsContainer));
            }
        });

        outerContainer.getChildren().add(paramsContainer);
        outerContainer.getChildren().add(confirmButton);

        return new Scene(outerContainer, containerWidth, containerHeight);
    }

    private GridPane getMapParametersContainer()
    {
        GridPane containerLayout = new GridPane();

        Label widthLabel = new Label("Map width:");
        TextField widthTextField = new TextField("10");
        widthLabel.setFont(new Font(20));
        containerLayout.add(widthLabel, 0, 0);
        containerLayout.add(widthTextField, 1, 0);

        Label heightLabel = new Label("Map height:");
        TextField heightTextField = new TextField("10");
        heightLabel.setFont(new Font(20));
        containerLayout.add(heightLabel, 0, 1);
        containerLayout.add(heightTextField, 1, 1);

        Label startEnergyLabel = new Label("Animal energy at start:");
        TextField startEnergyTextField = new TextField("100");
        startEnergyLabel.setFont(new Font(20));
        containerLayout.add(startEnergyLabel, 0, 2);
        containerLayout.add(startEnergyTextField, 1, 2);

        Label moveEnergyLabel = new Label("Move energy cost:");
        TextField moveEnergyTextField = new TextField("1");
        moveEnergyLabel.setFont(new Font(20));
        containerLayout.add(moveEnergyLabel, 0, 3);
        containerLayout.add(moveEnergyTextField, 1, 3);

        Label plantEnergyLabel = new Label("Plant eating energy:");
        TextField plantEnergyTextField = new TextField("100");
        plantEnergyLabel.setFont(new Font(20));
        containerLayout.add(plantEnergyLabel, 0, 4);
        containerLayout.add(plantEnergyTextField, 1, 4);

        Label jungleRatioLabel = new Label("Jungle ratio:");
        jungleRatioLabel.setFont(new Font(20));
        TextField jungleRatioTextField = new TextField("0.3");
        containerLayout.add(jungleRatioLabel, 0, 5);
        containerLayout.add(jungleRatioTextField, 1, 5);

        Label startAnimalsCountLabel = new Label("Starting animals count:");
        startAnimalsCountLabel.setFont(new Font(20));
        TextField startAnimalsCountTextField = new TextField("10");
        containerLayout.add(startAnimalsCountLabel, 0, 6);
        containerLayout.add(startAnimalsCountTextField, 1, 6);

        CheckBox isMagicMapCheckBox = new CheckBox("magic");
        isMagicMapCheckBox.setFont(new Font(20));
        containerLayout.add(isMagicMapCheckBox, 0, 7, 2, 1);

        for (int i=0; i < 2; i++)
            containerLayout.getColumnConstraints().add(new ColumnConstraints(containerWidth / 4));
        for (int i=0; i < 8; i++)
            containerLayout.getRowConstraints().add(new RowConstraints(containerHeight / 10));
        for (Node element: containerLayout.getChildren())
        {
            GridPane.setHalignment(element, HPos.CENTER);
        }

        return containerLayout;
    }

    public AbstractMap createMapWithParams(GridPane paramsContainer, boolean isSoftBound)
    {
        List<Node> children = paramsContainer.getChildren();
        int width, height, animalEnergy, moveCost, plantEnergy, animalsCount;
        double jungleRatio;
        boolean isMagic;
        width = Integer.parseInt(((TextField) children.get(1)).getText());
        height = Integer.parseInt(((TextField) children.get(3)).getText());
        animalEnergy = Integer.parseInt(((TextField) children.get(5)).getText());
        moveCost = Integer.parseInt(((TextField) children.get(7)).getText());
        plantEnergy = Integer.parseInt(((TextField) children.get(9)).getText());
        jungleRatio = Double.parseDouble(((TextField) children.get(11)).getText());
        isMagic = ((CheckBox) children.get(14)).isSelected();
        if (isMagic)
        {
            if (isSoftBound)
                return new SoftBoundMagicMap(width, height, animalEnergy,
                        plantEnergy, jungleRatio, moveCost);
            else
                return new HardBoundMagicMap(width, height, animalEnergy,
                        plantEnergy, jungleRatio, moveCost);
        }
        else
        {
            if (isSoftBound)
                return new SoftBoundMap(width, height, animalEnergy,
                        plantEnergy, jungleRatio, moveCost);
            else
                return new HardBoundMap(width, height, animalEnergy, plantEnergy,
                        jungleRatio, moveCost);
        }
    }

    private int getAnimalCount(GridPane paramsContainer)
    {
        return Integer.parseInt(((TextField) paramsContainer.getChildren().get(13)).getText());
    }
}
