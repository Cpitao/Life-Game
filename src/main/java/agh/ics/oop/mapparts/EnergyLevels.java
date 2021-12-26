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
            case VERY_GOOD -> Color.rgb(50, 3, 252);
            case GOOD -> Color.rgb(92, 59, 235);
            case LITTLE -> Color.rgb(143, 120, 245);
            case VERY_LITTLE -> Color.rgb(182, 168, 247);
            case CRITICAL -> Color.rgb(211, 203, 245);
        };
    }
}
