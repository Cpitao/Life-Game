package agh.ics.oop.maps;

import agh.ics.oop.mapparts.Vector2d;

public class HardBoundMagicMap extends AbstractMap {

    public HardBoundMagicMap(int width, int height, int animalsInitialEnergy,
                        int plantsEnergy, double jungleRatio, int moveCost) {
        super(width, height, animalsInitialEnergy, plantsEnergy, jungleRatio, moveCost);
    }

    @Override
    public Vector2d correctPosition(Vector2d position) {
        if (position.x == this.width)
            position = new Vector2d(position.x - 1, position.y);

        if (position.x == -1)
            position = new Vector2d(0, position.y);

        if (position.y == this.height)
            position = new Vector2d(position.x, position.y - 1);

        if (position.y == -1)
            position = new Vector2d(position.x, 0);

        return position;
    }
}
