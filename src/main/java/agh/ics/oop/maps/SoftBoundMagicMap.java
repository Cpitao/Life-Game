package agh.ics.oop.maps;

import agh.ics.oop.Vector2d;

public class SoftBoundMagicMap extends AbstractMagicMap{

    public SoftBoundMagicMap(int width, int height, int animalsInitialEnergy,
                        int plantsEnergy, double jungleRatio, int moveCost) {
        super(width, height, animalsInitialEnergy, plantsEnergy, jungleRatio, moveCost);
    }
    @Override
    public Vector2d correctPosition(Vector2d position) {
        if (position.x > this.width-1 || position.x < 0)
            position.x = (position.x + this.width) % this.width;

        if (position.y > this.height-1 || position.y < 0)
            position.y = (position.y+this.height) % this.height;

        return position;
    }
}
