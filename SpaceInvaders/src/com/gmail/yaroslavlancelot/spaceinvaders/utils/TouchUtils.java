package com.gmail.yaroslavlancelot.spaceinvaders.utils;

/** Helping functions to work with touch events */
public final class TouchUtils {
    private TouchUtils() {
    }

    /** Check is value is in range and return new value base in this information */
    public static float stickToBorderOrLeftValue(float value, float minValue, float maxValue) {
        if (value > maxValue)
            return maxValue;
        else if (value < minValue)
            return minValue;
        return value;
    }
}
