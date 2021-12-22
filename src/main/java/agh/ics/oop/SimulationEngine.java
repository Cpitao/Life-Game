package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.maps.IWorldMap;

import java.util.LinkedList;
import java.util.SortedSet;

public class SimulationEngine implements IEngine, Runnable{

    private static final int moveDelay = 300;
    private AbstractMap map;

    public SimulationEngine(AbstractMap map)
    {
        this.map = map;
    }

    @Override
    public void run() {
        moveAllAnimals();
        eatAll();
        reproduceAll();
        this.map.generatePlants();
    }

    private void moveAllAnimals()
    {
        Vector2d[] positions = this.map.getAnimals().keySet().toArray(new Vector2d[0]);
        for (Vector2d position: positions)
        {
            SortedSet<Animal> animalsAtPosition = this.map.getAnimals().get(position);
            for (Animal animal: animalsAtPosition)
            {
                animal.move();
            }
        }
    }

    private void eatAll()
    {
        Vector2d[] positions = this.map.getAnimals().keySet().toArray(new Vector2d[0]);
        for (Vector2d position: positions)
        {
            if (this.map.getPlants().get(position) != null)
            {
                int animalsEating = 0;
                int maxAnimalEnergyAtPosition = this.map.getAnimals().get(position).last().getEnergy();
                for (Animal animal: this.map.getAnimals().get(position))
                {
                    if (animal.getEnergy() == maxAnimalEnergyAtPosition)
                        animalsEating++;
                }

                for (Animal animal: this.map.getAnimals().get(position))
                {
                    if (animal.getEnergy() == maxAnimalEnergyAtPosition)
                        animal.eat(this.map.getPlants().get(position), animalsEating);
                }
                this.map.getPlants().remove(position);
            }
        }
    }

    private void reproduceAll()
    {
        Vector2d[] positions = this.map.getAnimals().keySet().toArray(new Vector2d[0]);
        for (Vector2d position: positions)
        {
            if (this.map.getAnimals().get(position).size() > 1)
            {
                Animal parent1 = this.map.getAnimals().get(position).last();
                this.map.getAnimals().get(position).remove(parent1);
                Animal parent2 = this.map.getAnimals().get(position).last();
                this.map.getAnimals().get(position).add(parent1);
                Animal kid = new Animal(parent1, parent2);
                this.map.place(kid);
            }
        }
    }

    public AbstractMap getMap() {
        return map;
    }
}
