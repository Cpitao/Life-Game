package agh.ics.oop.mapparts;

import agh.ics.oop.IMapElementRemovedObserver;
import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.Plant;
import agh.ics.oop.Vector2d;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

public class Animal implements IMapElement {

    private int energy;
    private final Genes genes;
    private Vector2d position;
    private MapDirection direction;
    private LinkedList<IPositionChangeObserver> positionChangeObservers = new LinkedList<>();
    private LinkedList<IMapElementRemovedObserver> objectRemovedObservers = new LinkedList<>();
    private final AbstractMap map;

    public Animal(AbstractMap map)
    {
        this.genes = new Genes();
        this.energy = map.getAnimalsInitialEnergy();
        this.direction = MapDirection.values()[new Random().nextInt(8)];

        this.position = map.getRandomFreeCell();

        this.map = map;
        this.addPositionChangeObserver(map);
        this.addDeathObserver(map);
    }

    public Animal(AbstractMap map, Genes genes)
    {
        this.genes = genes;
        this.energy = map.getAnimalsInitialEnergy();
        this.direction = MapDirection.values()[new Random().nextInt(8)];

        this.position = map.getRandomFreeCell();

        this.map = map;
        this.addPositionChangeObserver(map);
        this.addDeathObserver(map);
    }

    public Animal(AbstractMap map, Vector2d position)
    {
        this(map);
        this.position = position;
    }

    public Animal(Animal parent1, Animal parent2)
    {
        if (!parent1.getPosition().equals(parent2.getPosition()))
            throw new RuntimeException("Illegal animal reproduction encountered");

        this.position = parent1.getPosition();
        this.direction = MapDirection.values()[new Random().nextInt(8)];
        this.genes = new Genes(parent1, parent2);
        this.energy = parent1.getEnergy() / 4  + parent2.getEnergy() / 4;
        parent1.loseEnergy(parent1.getEnergy() / 4);
        parent2.loseEnergy(parent2.getEnergy() / 4);
        this.map = parent1.map;
        this.addPositionChangeObserver(parent1.map);
        this.addDeathObserver(parent1.map);
    }

    public void addPositionChangeObserver(IPositionChangeObserver observer)
    {
        this.positionChangeObservers.add(observer);
    }

    public void removePositionChangeObserver(IPositionChangeObserver observer)
    {
        this.positionChangeObservers.remove(observer);
    }

    public void addDeathObserver(IMapElementRemovedObserver deathObserver)
    {
        this.objectRemovedObservers.add(deathObserver);
    }

    public void removeDeathObserver(IMapElementRemovedObserver deathObserver)
    {
        this.objectRemovedObservers.remove(deathObserver);
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
        int randomGene = random.nextInt(32);
        int i=0;
        while (randomGene > 0 && i < 7)
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

        loseEnergy(map.getMoveCost());
    }

    public void eat(Plant plant, int sharedWith)
    {
        this.energy += map.getPlantsEnergy() / sharedWith;
    }

    public void die()
    {
        this.map.getAnimals().get(this.position).remove(this);
        for (IMapElementRemovedObserver deathObserver: this.objectRemovedObservers)
            deathObserver.mapElementRemoved(this);
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

    @Override
    public Color toColor() {
        return this.getEnergyLevel().toColor();
    }

    public String toString()
    {
        return this.direction.toString();
    }
}
