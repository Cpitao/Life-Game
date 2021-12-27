package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.Plant;
import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.observers.IEngine;
import agh.ics.oop.observers.IMapChangeObserver;

import java.util.LinkedList;

public class SimulationEngine implements IEngine, Runnable{

    public final Object lock = new Object();
    private int moveDelay = 100;  // setting this value too low will result in concurrent exception inside
    // MapVisualizer during map GUI update
    private AbstractMap map;
    private LinkedList<IMapChangeObserver> mapChangeObservers = new LinkedList<>();
    private boolean isRunning = false;

    public SimulationEngine(AbstractMap map, App app)
    {
        this.map = map;
        mapChangeObservers.add(app);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                if (!isRunning) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            removeDeadAnimals();
            moveAllAnimals();
            eatAll();
            reproduceAll();
            generatePlants();
            notifyMapChanged();
            map.newEra();
            map.getMapStatisticsTracker().newEra();

            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeDeadAnimals()
    {
        LinkedList<Animal> deadAnimals = new LinkedList<>();
        for (Vector2d position: map.getAnimals().keySet())
        {
            for (Animal animal: map.getAnimals().get(position))
            {
                if (animal.getEnergy() <= 0)
                    deadAnimals.add(animal);
            }
        }
        for (Animal animal: deadAnimals)
        {
            animal.die();
        }
    }

    private void moveAllAnimals()
    {
        Vector2d[] positions = this.map.getAnimals().keySet().toArray(new Vector2d[0]);
        LinkedList<Animal> allAnimals = new LinkedList<>();
        for (Vector2d position: positions)
        {
            allAnimals.addAll(this.map.getAnimals().get(position));
        }
        for (Animal animal: allAnimals)
            animal.move();
    }

    private void eatAll()
    {
        Vector2d[] positions = this.map.getAnimals().keySet().toArray(new Vector2d[0]);
        for (Vector2d position: positions)
        {
            if (this.map.getPlants().get(position) != null)
            {
                int animalsEating = 0;
                int maxAnimalEnergyAtPosition = this.map.getHighestEnergyAnimalAtPosition(position).getEnergy();
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
                this.map.getPlants().get(position).notifyPlantEaten();
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
                Animal parent1 = this.map.getHighestEnergyAnimalAtPosition(position);
                this.map.getAnimals().get(position).remove(parent1);
                Animal parent2 = this.map.getHighestEnergyAnimalAtPosition(position);
                this.map.getAnimals().get(position).add(parent1);

                if (parent1.getEnergy() >= map.getAnimalsInitialEnergy()/2 &&
                        parent2.getEnergy() >= map.getAnimalsInitialEnergy()/2)
                {
                    Animal kid = new Animal(parent1, parent2);
                    this.map.placeAnimal(kid);
                }
            }
        }
    }

    public void generatePlants()
    {
        Vector2d junglePlantPosition = map.getRandomJungleCell();
        if (junglePlantPosition != null)
        {
            Plant plant = new Plant(map, junglePlantPosition);
            map.placePlant(plant);
        }

        Vector2d savannahPlantPosition = map.getRandomSavannahCell();
        if (savannahPlantPosition != null)
        {
            Plant plant = new Plant(map, savannahPlantPosition);
            map.placePlant(plant);
        }
    }

    public void notifyMapChanged()
    {
        for (IMapChangeObserver observer: mapChangeObservers)
            observer.mapUpdated(this.map);
    }

    public void changeRunningState()
    {
        isRunning = !isRunning;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void increaseSpeed()
    {
        if (moveDelay >= 200)
            moveDelay -= 100;
    }

    public void decreaseSpeed()
    {
        moveDelay += 100;
    }

    public int getMoveDelay()
    {
        return moveDelay;
    }

    public AbstractMap getMap()
    {
        return map;
    }
}
