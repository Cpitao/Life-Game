package agh.ics.oop;

import agh.ics.oop.mapparts.IMapElement;
import agh.ics.oop.maps.IWorldMap;

public class Plant implements IMapElement {

    public final static String imageSource = "src/main/java/resources/grass.jpg";
    private Vector2d position;
    private int energy;
    private IWorldMap map;

    public Plant(IWorldMap map, Vector2d position, int energy)
    {
        this.position = position;
        this.energy = energy;
        this.map = map;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    @Override
    public Vector2d getPosition()
    {
        return this.position;
    }

    public String toString()
    {
        return "+" + this.energy;
    }

}
