package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

import org.andengine.util.math.MathUtils;

/** Common operation with unit path */
public class PathHelper {
    /**
     * if user going to check point than, because of unit inertia, unit should begin to move to another path point
     * when unit approximately reach goal point (in epsilon of goal point).
     * This value represent distance which is this epsilon value
     */
    public static final float MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT = 60;

    private PathHelper() {
    }

    /**
     * return distance between two points
     *
     * @param x1 first point abscissa
     * @param y1 first point ordinate
     * @param x2 second point abscissa
     * @param y2 second point ordinate
     * @return distance between two points
     */
    public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return MathUtils.distance(x1, y1, x2, y2);
    }

    /**
     * calculate, depending on passed abscissa, is unit should move from left to right or vice versa
     *
     * @param xPosition unit movement start position (generally it can be unit planet position)
     * @return true if unit should move from left to right and false if vice versa
     */
    public static boolean isLtrPath(float xPosition) {
        return isLeftSide(xPosition);
    }

    /**
     * calculate to which side of the screen the point belongs to (by passed abscissa)
     *
     * @param xPosition point abscissa
     * @return true if point belongs to the left side of the screen and false in other case
     */
    public static boolean isLeftSide(float xPosition) {
        return xPosition < SizeConstants.HALF_FIELD_WIDTH;
    }

}