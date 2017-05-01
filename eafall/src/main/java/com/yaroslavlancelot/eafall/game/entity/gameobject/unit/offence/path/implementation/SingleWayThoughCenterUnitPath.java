package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;

/** Used for tracking unit check point passing */
public class SingleWayThoughCenterUnitPath implements IUnitPath {
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
    private int mCurrentPointIndex;
    // array which holds unit check points x
    private float[] mX;
    private int mY;


    /**
     * Create path
     *
     * @param ltr true if unit should move from left to right and false if vice versa
     */
    public SingleWayThoughCenterUnitPath(boolean ltr) {
        float[] x;
        if (ltr) {
            x = xArrayForward;
        } else {
            x = xArrayBackward;
        }
        mX = x;
        mY = SizeConstants.HALF_FIELD_HEIGHT;
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
        // if we reach last point
        if (mCurrentPointIndex + 1 == mX.length) {
            nextPoint[0] = mX[mX.length - 1];
            return;
        }
        // if it's not
        float distanceToNextPoint = PathHelper.getDistanceBetweenPoints(
                mX[mCurrentPointIndex], mY, currentPoint[0], currentPoint[1]);
        if (distanceToNextPoint < PathHelper.MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT) {
            mCurrentPointIndex++;
        }
        nextPoint[0] = mX[mCurrentPointIndex];
        nextPoint[1] = mY;
    }

    @Override
    public int getTotalPathPoints() {
        return xArrayBackward.length;
    }

    @Override
    public void setCurrentPathPoint(int n) {
        mCurrentPointIndex = n;
    }

    @Override
    public int getCurrentPathPoint() {
        return mCurrentPointIndex;
    }
}
