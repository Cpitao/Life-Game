package agh.ics.oop.maps;

import agh.ics.oop.Vector2d;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

import java.util.Set;

public abstract class AbstractMagicMap extends AbstractMap {

    protected int clonedAnimalsCounter = 0;

    public AbstractMagicMap(int width, int height, int animalsInitialEnergy,
                            int plantsEnergy, double jungleRatio, int moveCost) {
        super(width, height, animalsInitialEnergy, plantsEnergy, jungleRatio, moveCost);
    }

    public void cloneAnimals()
    {
        Set<Vector2d> currentPositions = this.animals.keySet();

        for (Vector2d position: currentPositions)
        {
            for (Animal animal: this.animals.get(position))
            {
                this.placeAnimal(new Animal(this, animal.getGenes()));
            }
        }

        this.clonedAnimalsCounter++;
    }

    @Override
    public void mapElementRemoved(IMapElement mapElement)
    {
        super.mapElementRemoved(mapElement);
        if (mapElement instanceof Animal)
        {
            if (clonedAnimalsCounter < 3 && animals.values().size() == 5)
            {
                cloneAnimals();
            }
        }
    }
}
