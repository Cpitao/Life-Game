package agh.ics.oop.mapparts;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.maps.IWorldMap;
import agh.ics.oop.Plant;
import agh.ics.oop.Vector2d;

import java.util.LinkedList;
import java.util.Random;

public class Animal implements IMapElement {

    private int energy;
    private final Genes genes;
    private Vector2d position;
    private MapDirection direction;
    private LinkedList<IPositionChangeObserver> positionChangeObservers = new LinkedList<>();
    private LinkedList<IAnimalDeathObserver> deathObservers = new LinkedList<>();
    private final IWorldMap map;

    public Animal(IWorldMap map)
    {
        this.genes = new Genes();
        this.energy = map.getAnimalsInitialEnergy();

        Random random = new Random();
        Vector2d newPosition = map.getRandomFreeCell();
        this.position = newPosition;
        this.map = map;
        this.addPositionChangeObserver(map);
    }

    public Animal(IWorldMap map, Vector2d position)
    {
        this(map);
        this.position = position;
    }

    public Animal(Animal parent1, Animal parent2)
    {
        if (parent1.getPosition() != parent2.getPosition())
            throw new RuntimeException("Illegal animal reproduction encountered");

        parent1.loseEnergy(parent1.getEnergy() / 4);
        parent2.loseEnergy(parent2.getEnergy() / 4);

        this.position = parent1.getPosition();
        this.genes = new Genes(parent1, parent2);
        this.energy = parent1.getEnergy() / 4  + parent2.getEnergy() / 4;
        this.map = parent1.map;
        this.addPositionChangeObserver(parent1.map);
    }

    public void addPositionChangeObserver(IPositionChangeObserver observer)
    {
        this.positionChangeObservers.add(observer);
    }

    public void removePositionChangeObserver(IPositionChangeObserver observer)
    {
        this.positionChangeObservers.remove(observer);
    }

    public void addDeathObserver(IAnimalDeathObserver deathObserver)
    {
        this.deathObservers.add(deathObserver);
    }

    public void removeDeathObserver(IAnimalDeathObserver deathObserver)
    {
        this.deathObservers.remove(deathObserver);
    }
    public int getEnergy()
    {
        return this.energy;
    }

    public Genes getGenes()
    {
        return genes;
    }

    @Override
    public Vector2d getPosition()
    {
        return this.position;
    }

    /**
     *  function generating next move based on object's genes
     * @return next move to make
     */
    private MoveDirection generateRandomMove()
    {
        Random random = new Random();
        int randomGene = random.nextInt(32) + 1;
        int i=0;
        while (randomGene > 0)
        {
            randomGene -= this.genes.getGeneTypeCounts()[i];
            i += 1;
        }

        return MoveDirection.values[i];
    }

    public void move()
    {
        MoveDirection randomMove = this.generateRandomMove();
        switch(randomMove)
        {
            case FORWARD ->
                    {
                        Vector2d oldPosition = new Vector2d(this.position.x, this.position.y);
                        this.position = this.map.correctPosition(this.position.add(this.direction.toUnitVector()));
                        for (IPositionChangeObserver observer: this.positionChangeObservers)
                            observer.positionChanged(this, oldPosition, this.position);
                    }
            case BACKWARD ->
                    {
                        Vector2d oldPosition = new Vector2d(this.position.x, this.position.y);
                        this.position = this.map.correctPosition(this.position.subtract(this.direction.toUnitVector()));
                        for (IPositionChangeObserver observer: this.positionChangeObservers)
                            observer.positionChanged(this, oldPosition, this.position);
                    }
            default -> {
                for (int i=0; i < randomMove.ordinal(); i++)
                    this.direction = this.direction.next();
            }
        }
    }

    public void eat(Plant plant, int sharedWith)
    {
        this.energy += plant.getEnergy() / sharedWith;
    }

    public void die()
    {
        this.map.getAnimals().get(this.position).remove(this);
        for (IAnimalDeathObserver deathObserver: this.deathObservers)
            deathObserver.animalDied(this);
    }

    public void loseEnergy(int lostEnergy)
    {
        this.energy -= lostEnergy;
    }

    public EnergyLevels getEnergyLevel()
    {
        int initialEnergy = this.map.getAnimalsInitialEnergy();
        int ratio = 100 * this.energy / initialEnergy;

        if (ratio < 10)
            return EnergyLevels.CRITICAL;
        else if (ratio < 25)
            return EnergyLevels.VERY_LITTLE;
        else if (ratio < 50)
            return EnergyLevels.LITTLE;
        else if (ratio < 75)
            return EnergyLevels.GOOD;
        else
            return EnergyLevels.VERY_GOOD;
    }

    public String toString()
    {
        return this.direction.toString();
    }
}
