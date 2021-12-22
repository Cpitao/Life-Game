package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.SoftBoundMap;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;

public class App extends Application {

    private final int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width * 9 / 10;
    private final int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height * 8/10;
    private final int statsPanelWidth = windowWidth / 6;

    private GridPane leftMapContainer;
    private GridPane rightMapContainer;

    private GridPane leftMapGrid;
    private GridPane rightMapGrid;

    private SimulationEngine engine1;
    private SimulationEngine engine2;

    @Override
    public void init()
    {
        SoftBoundMap softBoundMap = new SoftBoundMap(20, 20, 10, 2, 0.5);
        for (int i=0; i < 100; i++)
            softBoundMap.place(new Animal(softBoundMap));


        leftMapContainer = new GridPane();
        leftMapContainer.getRowConstraints().add(new RowConstraints(windowHeight - 5));
        leftMapContainer.getColumnConstraints().add(new ColumnConstraints(windowWidth / 2 - statsPanelWidth));
        MapVisualizer mapVisualizer = new MapVisualizer(softBoundMap, leftMapContainer);
        mapVisualizer.drawMapElements();
        leftMapGrid = mapVisualizer.getGridPane();
        leftMapGrid.setGridLinesVisible(true);
        leftMapContainer.getChildren().add(leftMapGrid);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(leftMapContainer, windowWidth, windowHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    public Scene getInitialScene()
//    {
//    }

}
