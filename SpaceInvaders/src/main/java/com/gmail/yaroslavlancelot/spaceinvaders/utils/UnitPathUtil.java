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
    /** unit check point x coordinates if unit moves from left to right */
    private static final float[] xArray1 = new float[]{
            SizeConstants.PLANET_DIAMETER,
            SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH / 2,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2};
    /** unit check point x coordinates if unit moves from right to left */
    private static final float[] yArray = new float[]{
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 10,
            SizeConstants.GAME_FIELD_HEIGHT / 5,
            SizeConstants.GAME_FIELD_HEIGHT / 2 - SizeConstants.PLANET_DIAMETER / 2};
    /** unit check point y coordinates */
    private static final float[] xArray2 = new float[]{
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2,
            SizeConstants.GAME_FIELD_WIDTH - SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.GAME_FIELD_WIDTH / 2,
            SizeConstants.GAME_FIELD_WIDTH / 3,
            SizeConstants.PLANET_DIAMETER};

    private UnitPathUtil() {
    }

    /**
     * instantiate and returns {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil.UnitPath}
     * according to unit start position (is unit should to move from left to right or vice versa)
     *
     * @param startX unit start position
     *
     * @return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil.UnitPath} object
     */
    public static UnitPath getUnitPathAccordingToStartAbscissa(float startX) {
        float min = startX - xArray1[0];
        if (Math.abs(min) < Math.abs(startX - xArray2[0])) return new UnitPath(xArray1, yArray);
        else return new UnitPath(xArray2, yArray);
    }

    /**
     * return distance between two points
     *
     * @param x1 first point abscissa
     * @param y1 first point ordinate
     * @param x2 second point abscissa
     * @param y2 second point ordinate
     *
     * @return distance between two points
     */
    public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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
         * @param nextPoint value used to return next unit check point
         */
        public void getNextPathPoint(float[] currentPoint, float[] nextPoint) {
            getNextPathPoint(currentPoint, nextPoint, mX, mY);
        }

        /**
         * return new unit check point based on current position and previous position
         *
         * @param currentPoint current unit position point
         * @param nextPoint next unit check point position
         * @param xArray unit check points ordinate array
         * @param yArray unit check points abscissa array
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
            if (distanceToNextPoint < MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT)
                mCurrentPointIndex++;
            nextPoint[0] = xArray[mCurrentPointIndex];
            nextPoint[1] = yArray[mCurrentPointIndex];
        }
    }
}