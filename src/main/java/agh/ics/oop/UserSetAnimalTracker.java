package agh.ics.oop;

import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.Genes;
import agh.ics.oop.observers.IAnimalDeathObserver;
import agh.ics.oop.observers.IChildrenCountObserver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class UserSetAnimalTracker implements IChildrenCountObserver, IAnimalDeathObserver {

    private Animal trackedAnimal;
    private int deathEra;

    private Set<Animal> children = new HashSet<>();
    private Set<Animal> descendants = new HashSet<>();

    public UserSetAnimalTracker(Animal animal)
    {
        this.trackedAnimal = animal;
        animal.setTracker(this);
    }

    public void addKid(Animal animal)
    {
        this.children.add(animal);
        animal.addChildrenCountObserver(this);
    }

    @Override // further descendants, as opposed to addKid with only direct children
    public void newKid(Animal animal) {
        this.descendants.add(animal);
        animal.addChildrenCountObserver(this);
    }

    public void unsetTracker() {
        trackedAnimal.unsetTracker();
        for (Animal animal: children)
        {
            animal.removeChildrenCountObserver(this);
        }

        for (Animal animal: descendants)
        {
            animal.removeChildrenCountObserver(this);
        }
    }

    @Override
    public void animalDied(Animal animal) {
        this.deathEra = animal.getMap().getEra();
    }

    public Animal getTrackedAnimal()
    {
        return this.trackedAnimal;
    }

    public int getAnimalEnergy()
    {
        return trackedAnimal.getEnergy();
    }

    public int getChildrenCount()
    {
        return children.size();
    }

    public int getDescendantsCount()
    {
        return children.size() + descendants.size();
    }

    public Genes getAnimalGenes()
    {
        return trackedAnimal.getGenes();
    }

    public Integer getDeathEra()
    {
        return deathEra;
    }
}
