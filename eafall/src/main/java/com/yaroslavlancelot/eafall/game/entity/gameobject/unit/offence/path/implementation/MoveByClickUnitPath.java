package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;

/** Used with move-by-click units (manual control) */
public class MoveByClickUnitPath implements IUnitPath {
    private float mX;
    private float mY;

    public MoveByClickUnitPath(float x, float y) {
        this.mX = x;
        this.mY = y;
    }

    public void setNewPoint(float x, float y) {
        mX = x;
        mY = y;
    }

    @Override
    public void getNextPathPoint(float[] currentPoint, float[] nextPoint) {
        nextPoint[0] = mX;
        nextPoint[1] = mY;
    }

    @Override
    public int getTotalPathPoints() {
        return 1;
    }

    @Override
    public void setCurrentPathPoint(int n) {
    }

    @Override
    public int getCurrentPathPoint() {
        return 0;
    }
}
