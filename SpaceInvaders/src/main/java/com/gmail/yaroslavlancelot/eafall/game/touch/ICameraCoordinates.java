package com.gmail.yaroslavlancelot.eafall.game.touch;

/**
 * For getting coordinates of current scene position
 */
public interface ICameraCoordinates {
    float getCameraCurrentWidth();

    float getCameraCurrentHeight();

    float getCameraCurrentCenterX();

    float getCameraCurrentCenterY();

    public float getTargetZoomFactor();

    public float getMaxZoomFactorChange();
}
