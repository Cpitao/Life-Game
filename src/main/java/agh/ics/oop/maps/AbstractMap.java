package agh.ics.oop.maps;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.Plant;
import agh.ics.oop.Vector2d;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.EnergyLevels;
import agh.ics.oop.mapparts.IMapElement;

import java.util.*;

public abstract class AbstractMap implements IWorldMap, IPositionChangeObserver, IAnimalDeathObserver {

    // map parameters
    protected int width;
    protected int height;
    // user input
    protected int jungleRatio;
    protected int animalsInitialEnergy;
    protected int plantsEnergy;

    protected Vector2d jungleLowerLeftCorner;
    protected Vector2d jungleUpperRightCorner;
    // objects on the map
    protected HashMap<Vector2d, SortedSet<Animal>> animals = new HashMap<>();
    protected HashMap<Vector2d, Plant> plants = new HashMap<>();
    // free cells
    protected LinkedList<Vector2d> freeJungleCells = new LinkedList<>();
    protected LinkedList<Vector2d> freeSavannahCells = new LinkedList<>();

    private static final Comparator<Animal> animalComparator = Comparator.comparingInt(Animal::getEnergy);

    public AbstractMap(int width, int height, int animalsInitialEnergy, int plantsEnergy, double jungleRatio)
    {
        this.width = width;
        this.height = height;

        this.jungleLowerLeftCorner = new Vector2d((int) jungleRatio * (1 - width) / 2,
                (int) jungleRatio * (1 - height) / 2);
        System.out.println(10);
        this.animalsInitialEnergy = animalsInitialEnergy;
        this.plantsEnergy = plantsEnergy;
        for (int i=0; i < width; i++)
            for (int j=0; j < height; j++) {
                addFreePosition(new Vector2d(i, j));
            }
    }

    public void place(Animal animal)
    {
        this.animals.computeIfAbsent(animal.getPosition(), k -> new TreeSet<>(animalComparator));
        this.animals.get(animal.getPosition()).add(animal);
        this.freeJungleCells.remove(animal.getPosition());
    }

    public void generatePlants()
    {
        Random random = new Random();
        // generate plant inside the jungle
        Vector2d newPosition = new Vector2d(
                width / 2 - width * jungleRatio / 2 + random.nextInt(width * jungleRatio),
                height / 2 - height * jungleRatio / 2 + random.nextInt(height * jungleRatio));

        while (this.plants.containsKey(newPosition))
            newPosition = new Vector2d(
                    width / 2 - width * jungleRatio / 2 + random.nextInt(width * jungleRatio),
                    height / 2 - height * jungleRatio / 2 + random.nextInt(height * jungleRatio));

        this.plants.put(newPosition, new Plant(this, newPosition, plantsEnergy));

        // generate plant outside the jungle
        newPosition = new Vector2d(
                random.nextInt(width / 2 - width * jungleRatio) + random.nextInt(2)*width*jungleRatio,
                random.nextInt(height / 2 - height * jungleRatio) + random.nextInt(2)*height*jungleRatio
        );
        while (this.plants.containsKey(newPosition))
            newPosition = new Vector2d(
                    random.nextInt(width / 2 - width * jungleRatio) + random.nextInt(2)*width*jungleRatio,
                    random.nextInt(height / 2 - height * jungleRatio) + random.nextInt(2)*height*jungleRatio
            );

        this.plants.put(newPosition, new Plant(this, newPosition, plantsEnergy));
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
            this.freeJungleCells.add(oldPosition);
        }
        this.animals.computeIfAbsent(newPosition, k -> new TreeSet<>(animalComparator));
        this.animals.get(newPosition).add(animal);
    }

    @Override
    public void animalDied(Animal animal) {
        this.animals.get(animal.getPosition()).remove(animal);
        if (this.animals.get(animal.getPosition()).size() == 0)
        {
            this.animals.remove(animal.getPosition());
            this.freeJungleCells.add(animal.getPosition());
        }
    }

    @Override
    public HashMap<Vector2d, SortedSet<Animal>> getAnimals() {
        return animals;
    }

    @Override
    public HashMap<Vector2d, Plant> getPlants()
    {
        return plants;
    }

    @Override
    public IMapElement objectAt(Vector2d position)
    {
        if (this.animals.get(position) != null)
            return this.animals.get(position).last();

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
        int randomPositionNumber = new Random().nextInt(freeJungleCells.size());
        return freeJungleCells.get(randomPositionNumber);
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
            if (!this.freeJungleCells.contains(position))
                this.freeJungleCells.add(position);
        else
            if (!this.freeSavannahCells.contains(position))
                this.freeSavannahCells.add(position);
    }
    public abstract Vector2d correctPosition(Vector2d position);

}
