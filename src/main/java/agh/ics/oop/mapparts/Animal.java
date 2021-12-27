package agh.ics.oop.mapparts;

import agh.ics.oop.UserSetAnimalTracker;
import agh.ics.oop.maps.AbstractMap;
import agh.ics.oop.observers.IAnimalEnergyChangeObserver;
import agh.ics.oop.observers.IChildrenCountObserver;
import agh.ics.oop.observers.IMapElementRemovedObserver;
import agh.ics.oop.observers.IPositionChangeObserver;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

public class Animal implements IMapElement {

    private int energy;
    private Genes genes;
    private Vector2d position;
    private MapDirection direction;
    private final AbstractMap map;

    private LinkedList<IPositionChangeObserver> positionChangeObservers = new LinkedList<>();
    private LinkedList<IMapElementRemovedObserver> objectRemovedObservers = new LinkedList<>();
    private LinkedList<IAnimalEnergyChangeObserver> animalEnergyChangeObservers = new LinkedList<>();
    private LinkedList<IChildrenCountObserver> childrenCountObservers = new LinkedList<>();

    private UserSetAnimalTracker tracker = null;
    private final int bornIn;
    private int kids = 0;

    public Animal(AbstractMap map)
    {
        this.genes = new Genes();
        this.energy = map.getAnimalsInitialEnergy();
        this.direction = MapDirection.values()[new Random().nextInt(8)];

        this.position = map.getRandomFreeCell();

        this.map = map;
        bornIn = map.getEra();
        this.addPositionChangeObserver(map);
        this.addDeathObserver(map);
        this.animalEnergyChangeObservers.add(map.getMapStatisticsTracker().getAnimalStatsTracker());
        this.childrenCountObservers.add(map.getMapStatisticsTracker().getAnimalStatsTracker());
    }

    public Animal(AbstractMap map, Genes genes)
    {
        this(map);
        this.genes = genes;
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
        parent1.addKid(this);
        parent2.addKid(this);

        this.energy = parent1.getEnergy() / 4  + parent2.getEnergy() / 4;
        parent1.loseEnergy(parent1.getEnergy() / 4);
        parent2.loseEnergy(parent2.getEnergy() / 4);

        this.map = parent1.map;
        bornIn = map.getEra();

        this.addPositionChangeObserver(parent1.map);
        this.addDeathObserver(parent1.map);
        this.animalEnergyChangeObservers.add(map.getMapStatisticsTracker().getAnimalStatsTracker());
        this.childrenCountObservers.add(map.getMapStatisticsTracker().getAnimalStatsTracker());
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

    public void addChildrenCountObserver(IChildrenCountObserver observer)
    {
        this.childrenCountObservers.add(observer);
    }

    public void removeChildrenCountObserver(IChildrenCountObserver observer)
    {
        this.childrenCountObservers.remove(observer);
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
        notifyEnergyChanged(map.getPlantsEnergy() / sharedWith);
    }

    public void die()
    {
        this.map.getAnimals().get(this.position).remove(this);
        for (IMapElementRemovedObserver deathObserver: this.objectRemovedObservers)
            deathObserver.mapElementRemoved(this);
        if (tracker != null)
        {
            tracker.animalDied(this);
        }
    }

    public void loseEnergy(int lostEnergy)
    {
        this.energy -= lostEnergy;
        notifyEnergyChanged(-lostEnergy);
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

    public void notifyEnergyChanged(int energy)
    {
        for (IAnimalEnergyChangeObserver observer: animalEnergyChangeObservers)
        {
            observer.energyChanged(energy);
        }
    }

    @Override
    public Color toColor() {
        return this.getEnergyLevel().toColor();
    }

    public String toString()
    {
        return this.direction.toString();
    }

    public int getAge()
    {
        return map.getEra() - this.bornIn;
    }

    public void addKid(Animal animal)
    {
        kids++;
        notifyNewKid(animal);

        if (tracker != null)
            tracker.addKid(animal);
    }

    public void notifyNewKid(Animal animal)
    {
        for (IChildrenCountObserver observer: childrenCountObservers)
            observer.newKid(animal);
    }

    public int getKids()
    {
        return kids;
    }

    public void setTracker(UserSetAnimalTracker tracker)
    {
        this.tracker = tracker;
    }

    public void unsetTracker()
    {
        this.tracker = null;
    }

    public AbstractMap getMap()
    {
        return this.map;
    }
}
