package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

/**
 * Path class which sends everything to the center of the game screen.
 *
 * @author Yaroslav Havrylovych
 */
public class MoveToPointThroughTheCenterPath extends BasePath {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private final float mPointX;
    private final float mPointY;
    private final float EPSILON = SizeConstants.HALF_FIELD_WIDTH / 4;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MoveToPointThroughTheCenterPath(float x, float y) {
        mPointX = x;
        mPointY = y;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void getNextPathPoint(final float[] currentPoint, final float[] nextPoint) {
        if (Math.abs(mPointX - currentPoint[0]) > EPSILON) {
            nextPoint[1] = currentPoint[1];
        } else {
            nextPoint[1] = mPointY;
        }
        nextPoint[0] = mPointX;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
