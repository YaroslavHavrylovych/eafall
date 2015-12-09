package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;

/**
 * Path class which sends everything to the center of the game screen.
 *
 * @author Yaroslav Havrylovych
 */
public class MoveToCenterPath extends BasePath {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void getNextPathPoint(final float[] currentPoint, final float[] nextPoint) {
        nextPoint[0] = SizeConstants.HALF_FIELD_WIDTH;
        nextPoint[1] = SizeConstants.HALF_FIELD_HEIGHT;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
