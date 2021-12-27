package agh.ics.oop.maps;

import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractMagicMap extends AbstractMap {

    protected int clonedAnimalsCounter = 0;

    public AbstractMagicMap(int width, int height, int animalsInitialEnergy,
                            int plantsEnergy, double jungleRatio, int moveCost) {
        super(width, height, animalsInitialEnergy, plantsEnergy, jungleRatio, moveCost);
    }

    public void cloneAnimals(LinkedList<Animal> animals)
    {
        for (Animal animal : animals)
            this.placeAnimal(new Animal(this, animal.getGenes()));

        this.clonedAnimalsCounter++;
    }

    @Override
    public void mapElementRemoved(IMapElement mapElement)
    {
        super.mapElementRemoved(mapElement);
        if (mapElement instanceof Animal)
        {
            if (clonedAnimalsCounter < 3)
            {
                LinkedList<Animal> animals = new LinkedList<>();
                for (Vector2d position: this.animals.keySet())
                {
                    for (Animal animal: this.animals.get(position))
                        animals.add(animal);
                }
                if (animals.size() == 5)
                    cloneAnimals(animals);
            }
        }
    }
}
