package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path;

/** Unit path interface. To not be assigned to concrete path implementation. */
public interface IUnitPath {
    /**
     * calculate new coordinates based on current
     *
     * @param currentPoint two dimensional array with [x,y] current position
     * @param nextPoint    two dimensional array with [x,y] next position
     */
    void getNextPathPoint(float[] currentPoint, float[] nextPoint);
}
