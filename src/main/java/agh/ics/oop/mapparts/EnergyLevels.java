package agh.ics.oop.mapparts;

import javafx.scene.paint.Color;

public enum EnergyLevels {
    VERY_GOOD, GOOD, LITTLE, VERY_LITTLE, CRITICAL;
    /**
     * Values correspond to percentage levels of animals energy.
     * VERY_GOOD - 75%+
     * GOOD - 50% -75%
     * LITTLE - 25% - 50%
     * VERY_LITTLE - 10% - 25%
     * CRITICAL - 0% - 10%
     * Percent of initial energy.
     */

    public Color toColor()
    {
        return switch(this)
        {
            case VERY_GOOD -> Color.rgb(0, 0, 255);
            case GOOD -> Color.rgb(200, 36, 237);
            case LITTLE -> Color.rgb(135, 62, 199);
            case VERY_LITTLE -> Color.rgb(237, 36, 106);
            case CRITICAL -> Color.rgb(153, 0, 0);
        };
    }
}
