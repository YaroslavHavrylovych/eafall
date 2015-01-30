package com.gmail.yaroslavlancelot.spaceinvaders.objects;

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
