package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;

import org.andengine.util.math.MathUtils;

/** Common operation with unit path */
public class StaticHelper {
    /**
     * if user going to check point than, because of unit inertia, unit should begin to move to another path point
     * when unit approximately reach goal point (in epsilon of goal point).
     * This value represent distance which is this epsilon value
     */
    public static final float MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT = 60;
    /** left to right unit movement */
    private static final float[] xArrayForward = new float[]{
            SizeConstants.PLANET_DIAMETER,
            SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH / 2,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2};
    /** right to left unit movement */
    private static final float[] xArrayBackward = new float[]{
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH / 2,
            SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.PLANET_DIAMETER / 2};
    /** unit movement on the bottom of the screen */
    private static final float[] yArrayBottom = new float[]{
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 10,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2};
    /** unit movement on the top of the screen */
    private static final float[] yArrayTop = new float[]{
            SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_HEIGHT * 4 / 5,
            SizeConstants.GAME_FIELD_HEIGHT * 9 / 10,
            SizeConstants.GAME_FIELD_HEIGHT * 4 / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.PLANET_DIAMETER / 2};

    private StaticHelper() {
    }

    /**
     * instantiate and return unit movement path depending on given direction (should unit move
     * from left right and at the top or at the bottom)
     *
     * @param ltr true if unit should move from left to right and false if vice versa
     * @param top true if unit should move at the top of the screen
     * @return new {@link IUnitPath} instance
     */
    public static IUnitPath createUnitPath(boolean ltr, boolean top) {
        float[] x, y;
        if (ltr) {
            x = xArrayForward;
        } else {
            x = xArrayBackward;
        }
        if (top) {
            y = yArrayTop;
        } else {
            y = yArrayBottom;
        }
        return new UnitPath(x, y);
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
        float min = xPosition - xArrayForward[0];
        if (Math.abs(min) < Math.abs(xPosition - xArrayBackward[0])) {
            return true;
        } else {
            return false;
        }
    }

}