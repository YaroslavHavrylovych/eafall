package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

/** Used for tracking unit check point passing */
public class TwoWaysUnitPath extends BasePath {
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
    // arrays which holds unit check points x and y coordinates respectively
    private float[] mX, mY;


    /**
     * Create path
     *
     * @param ltr true if unit should move from left to right and false if vice versa
     * @param top true if unit should move at the top of the screen
     */
    public TwoWaysUnitPath(boolean ltr, boolean top) {
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
}
