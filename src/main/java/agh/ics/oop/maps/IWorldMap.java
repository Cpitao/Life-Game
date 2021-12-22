package agh.ics.oop.maps;

import agh.ics.oop.IPositionChangeObserver;
import agh.ics.oop.Plant;
import agh.ics.oop.Vector2d;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.IMapElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedSet;

public interface IWorldMap extends IPositionChangeObserver {
    /**
     * Function places an animal on the map
     * @param animal - animal to add to map
     */
    void place(Animal animal);

    /**
     * Function returns object at given position. Animals are prioritized over other elements. If there are many
     * animals at one position, pick one with the most energy.
     * @param position - position to check for objects
     */
    IMapElement objectAt(Vector2d position);

    /**
     * Function returns correct position on the map depending on its boundaries type
     * @param position - position to be verified and/or corrected
     * @return - return correct position within map boundaries
     */
    Vector2d correctPosition(Vector2d position);

    /**
     * Function to generate a plant in the jungle and outside it.
     */
    void generatePlants();

    int getAnimalsInitialEnergy();

    Vector2d getRandomFreeCell();

    int getWidth();

    int getHeight();

    HashMap<Vector2d, SortedSet<Animal>> getAnimals();
    HashMap<Vector2d, Plant> getPlants();
}