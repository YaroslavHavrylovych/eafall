package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;

/**
 * Base path class. Contains common methods.
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BasePath implements IUnitPath {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** current check point which unt should to pass */
    protected int mCurrentPointIndex;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * return new unit check point based on current position and previous position
     *
     * @param currentPoint current unit position point
     * @param nextPoint    next unit check point position
     * @param xArray       unit check points ordinate array
     * @param yArray       unit check points abscissa array
     */
    protected void getNextPathPoint(float[] currentPoint, float[] nextPoint, final float[] xArray, final float[] yArray) {
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
