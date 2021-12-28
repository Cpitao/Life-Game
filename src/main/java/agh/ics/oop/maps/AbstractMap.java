package agh.ics.oop.maps;

import agh.ics.oop.stattrackers.UserSetAnimalTracker;
import agh.ics.oop.mapparts.Plant;
import agh.ics.oop.mapparts.Vector2d;
import agh.ics.oop.observers.IMapElementRemovedObserver;
import agh.ics.oop.observers.INewEraObserver;
import agh.ics.oop.observers.IPositionChangeObserver;
import agh.ics.oop.stattrackers.MapStatsTracker;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

import java.util.*;

public abstract class AbstractMap implements IPositionChangeObserver, IMapElementRemovedObserver, INewEraObserver {

    // map parameters
    protected int width;
    protected int height;
    protected UserSetAnimalTracker userSetAnimalTracker = null;
    // user input
    protected double jungleRatio;
    protected int animalsInitialEnergy;
    protected int plantsEnergy;
    protected int moveCost;

    protected int era = 0;

    protected Vector2d jungleLowerLeftCorner;
    protected Vector2d jungleUpperRightCorner;

    // objects on the map
    protected HashMap<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    protected HashMap<Vector2d, Plant> plants = new HashMap<>();
    // free cells
    protected LinkedList<Vector2d> freeJungleCells = new LinkedList<>();
    protected LinkedList<Vector2d> freeSavannahCells = new LinkedList<>();

    protected MapStatsTracker mapStatsTracker = new MapStatsTracker();

    public AbstractMap(int width, int height, int animalsInitialEnergy,
                       int plantsEnergy, double jungleRatio, int moveCost)
    {

        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.moveCost = moveCost;

        this.animalsInitialEnergy = animalsInitialEnergy;
        this.plantsEnergy = plantsEnergy;

        setJungleBounds(width, height, jungleRatio);
        initializeFreeCells();
    }

    public void setJungleBounds(int width, int height, double jungleRatio)
    {
        this.jungleLowerLeftCorner = new Vector2d((int) (width * (1 - jungleRatio) / 2),
                (int) (height * (1 - jungleRatio) / 2));
        this.jungleUpperRightCorner = new Vector2d((int) (width * (1 + jungleRatio) / 2) - 1,
                (int) (height * (1 + jungleRatio) / 2) - 1);
    }

    public void initializeFreeCells()
    {
        for (int i=0; i < width; i++)
            for (int j=0; j < height; j++) {
                this.addFreePosition(new Vector2d(i, j));
            }
    }

    public void placeAnimal(Animal animal)
    {
        this.animals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());
        this.animals.get(animal.getPosition()).add(animal);
        this.removeFreePosition(animal.getPosition());

        mapStatsTracker.newAnimal(animal);
    }

    public void placePlant(Plant plant)
    {
        this.plants.put(plant.getPosition(), plant);
        this.removeFreePosition(plant.getPosition());

        mapStatsTracker.newPlant();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.animals.get(oldPosition).remove(animal);
        if (this.animals.get(oldPosition).size() == 0)
        {
            this.animals.remove(oldPosition);
            addFreePosition(oldPosition);
        }

        this.animals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());
        this.animals.get(animal.getPosition()).add(animal);
        this.removeFreePosition(animal.getPosition());
    }

    @Override
    public void mapElementRemoved(IMapElement element) {
        if (element instanceof Animal) {
            this.animals.get(element.getPosition()).remove(element);

            if (this.animals.get(element.getPosition()).size() == 0)
            {
                this.animals.remove(element.getPosition());
                addFreePosition(element.getPosition());
            }

            mapStatsTracker.animalDied((Animal) element);
        }
        else if (element instanceof Plant)
        {
            this.plants.remove(element.getPosition());

            mapStatsTracker.plantEaten();
        }
    }

    public HashMap<Vector2d, LinkedList<Animal>> getAnimals() {
        return animals;
    }

    public HashMap<Vector2d, Plant> getPlants()
    {
        return plants;
    }

    public IMapElement objectAt(Vector2d position)
    {
        if (this.animals.get(position) != null)
        {
            return getHighestEnergyAnimalAtPosition(position);
        }
        if (this.plants.get(position) != null)
            return this.plants.get(position);

        return null;
    }

    public int getAnimalsInitialEnergy()
    {
        return this.animalsInitialEnergy;
    }

    public Vector2d getRandomFreeCell()
    {
        int randomPositionNumber = new Random().nextInt(freeJungleCells.size() + freeSavannahCells.size());
        if (randomPositionNumber < freeJungleCells.size())
            return freeJungleCells.get(randomPositionNumber);
        else
            return freeSavannahCells.get(randomPositionNumber - freeJungleCells.size());
    }

    public Vector2d getRandomJungleCell()
    {
        if (freeJungleCells.size() == 0)
            return null;
        int randomPositionNumber = new Random().nextInt(freeJungleCells.size());
        return this.freeJungleCells.get(randomPositionNumber);
    }

    public Vector2d getRandomSavannahCell()
    {
        if (freeSavannahCells.size() == 0)
            return null;
        int randomPositionNumber = new Random().nextInt(freeSavannahCells.size());
        return this.freeSavannahCells.get(randomPositionNumber);
    }

    private boolean isPositionInJungle(Vector2d position)
    {
        return position.follows(jungleLowerLeftCorner) && position.precedes(jungleUpperRightCorner);
    }

    private void removeFreePosition(Vector2d position)
    {
        if (isPositionInJungle(position))
            this.freeJungleCells.remove(position);
        else
            this.freeSavannahCells.remove(position);
    }

    private void addFreePosition(Vector2d position)
    {
        if (isPositionInJungle(position))
        {
            if (!this.freeJungleCells.contains(position))
                this.freeJungleCells.add(position);
        }
        else
        {
            if (!this.freeSavannahCells.contains(position))
                this.freeSavannahCells.add(position);
        }
    }

    public int getMoveCost()
    {
        return this.moveCost;
    }

    public int getPlantsEnergy() {
        return plantsEnergy;
    }

    public Animal getHighestEnergyAnimalAtPosition(Vector2d position)
    {
        Animal strongestAnimal = this.animals.get(position).get(0);
        for (Animal animal: this.animals.get(position))
        {
            if (animal.getEnergy() > strongestAnimal.getEnergy())
            {
                strongestAnimal = animal;
            }
        }

        return strongestAnimal;
    }
    public abstract Vector2d correctPosition(Vector2d position);

    public MapStatsTracker getMapStatisticsTracker() {
        return mapStatsTracker;
    }

    public void newEra()
    {
        era++;
    }

    public int getEra()
    {
        return era;
    }

    public void setUserSetAnimalTracker(Animal animal)
    {
        this.userSetAnimalTracker = new UserSetAnimalTracker(animal);
    }

    public void unsetUserSetAnimalTracker()
    {
        if (this.userSetAnimalTracker != null) {
            this.userSetAnimalTracker.unsetTracker();
            this.userSetAnimalTracker = null;
        }
    }

    public UserSetAnimalTracker getUserSetAnimalTracker()
    {
        return userSetAnimalTracker;
    }
}
