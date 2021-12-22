package agh.ics.oop.maps;

import agh.ics.oop.Vector2d;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public abstract class AbstractMagicMap extends AbstractMap {

    protected int clonedAnimalsCounter = 0;

    public AbstractMagicMap(int width, int height, int animalsInitialEnergy, int plantsEnergy, double jungleRatio) {
        super(width, height, animalsInitialEnergy, plantsEnergy, jungleRatio);
    }

    public void cloneAnimals()
    {
        Set<Vector2d> currentPositions = this.animals.keySet();

        for (Vector2d position: currentPositions)
        {
            for (Animal animal: this.animals.get(position))
            {
                Random random = new Random();
                Vector2d newPosition = new Vector2d(random.nextInt(this.width), random.nextInt(this.height));
                while (currentPositions.contains(newPosition))
                    newPosition = new Vector2d(random.nextInt(this.width), random.nextInt(this.height));

                this.place(new Animal(this));
            }
        }

        this.clonedAnimalsCounter++;
    }
}
