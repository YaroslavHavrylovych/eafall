package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path;

/** Used for tracking unit check point passing */
public class UnitPath implements IUnitPath {
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
    UnitPath(float[] x, float[] y) {
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
    @Override
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
        float distanceToNextPoint = PathHelper.getDistanceBetweenPoints(xArray[mCurrentPointIndex], yArray[mCurrentPointIndex], x, y);
        if (distanceToNextPoint < PathHelper.MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT) {
            mCurrentPointIndex++;
        }
        nextPoint[0] = xArray[mCurrentPointIndex];
        nextPoint[1] = yArray[mCurrentPointIndex];
    }
}
