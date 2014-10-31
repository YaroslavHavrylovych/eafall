package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;

/** Common operation with unit path */
public class UnitPathUtil {
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
    /** unit movement on the top of the screen */
    private static final float[] yArrayTop = new float[]{
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 10,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2};
    /** unit movement on the bottom of the screen */
    private static final float[] yArrayBottom = new float[]{
            SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_HEIGHT * 4 / 5,
            SizeConstants.GAME_FIELD_HEIGHT * 9 / 10,
            SizeConstants.GAME_FIELD_HEIGHT * 4 / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 2 + SizeConstants.PLANET_DIAMETER / 2};

    private UnitPathUtil() {
    }

    /**
     * instantiate and return unit movement path depending on given direction (should unit move
     * from left right and at the top or at the bottom)
     *
     * @param ltr true if unit should move from left to right and false if vice versa
     * @param top true if unit should move at the top of the screen
     * @return new {@link UnitPath} instance
     */
    public static UnitPath createUnitPath(boolean ltr, boolean top) {
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
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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

    /** Used for tracking unit check point passing */
    public static class UnitPath {
        // arrays which holds unit check points x and y coordinates respectively
        private float[] mX, mY;
        /** current check point which unt should to pass */
        private int mCurrentPointIndex;

        /**
         * create new instance according to x and y check point array
         *
         * @param x check point abscissa array
         * @param y check points ordinate array
         */
        private UnitPath(float[] x, float[] y) {
            mX = x;
            mY = y;
            mCurrentPointIndex = 1;
        }

        /**
         * return next unit check point in nextPoint parameter
         *
         * @param currentPoint current unit position
         * @param nextPoint    value used to return next unit check point
         */
        public void getNextPathPoint(float[] currentPoint, float[] nextPoint) {
            getNextPathPoint(currentPoint, nextPoint, mX, mY);
        }

        /**
         * return new unit check point based on current position and previous position
         *
         * @param currentPoint current unit position point
         * @param nextPoint    next unit check point position
         * @param xArray       unit check points ordinate array
         * @param yArray       unit check points abscissa array
         */
        private void getNextPathPoint(float[] currentPoint, float[] nextPoint, final float[] xArray, final float[] yArray) {
            float x = currentPoint[0], y = currentPoint[1];
            // if we reach last point
            if (mCurrentPointIndex + 1 == xArray.length) {
                nextPoint[0] = xArray[xArray.length - 1];
                nextPoint[1] = yArray[yArray.length - 1];
                return;
            }
            // if it's not
            float distanceToNextPoint = getDistanceBetweenPoints(xArray[mCurrentPointIndex], yArray[mCurrentPointIndex], x, y);
            if (distanceToNextPoint < MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT) {
                mCurrentPointIndex++;
            }
            nextPoint[0] = xArray[mCurrentPointIndex];
            nextPoint[1] = yArray[mCurrentPointIndex];
        }
    }
}