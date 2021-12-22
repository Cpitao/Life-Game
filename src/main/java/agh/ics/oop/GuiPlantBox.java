package agh.ics.oop;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiPlantBox {

    private ImageView imageView;
    private Label label;
    private VBox vbox;

    public GuiPlantBox(Plant plant)
    {
        try {
            Image image = new Image(new FileInputStream(Plant.imageSource));
            imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            vbox = new VBox();
            vbox.getChildren().addAll(imageView, label);
            vbox.setAlignment(Pos.CENTER);

        } catch (FileNotFoundException e) {
            System.out.println("Image file does not exist");
        }
    }

    public VBox getVbox()
    {
        return this.vbox;
    }
}

