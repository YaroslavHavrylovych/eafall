package com.yaroslavlancelot.eafall.game.touch;

/**
 * For getting coordinates of current camera to scene position
 */
public interface ICameraHandler {
    float getMaxX();

    float getMaxY();

    float getMinX();

    float getMinY();

    float getZoomFactor();
}
