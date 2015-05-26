package com.gmail.yaroslavlancelot.eafall.game.touch;

/**
 * For getting coordinates of current camera to scene position
 */
public interface ICameraHandler {
    float getWidth();

    float getHeight();

    float getCenterX();

    float getCenterY();

    float getZoomFactor();

    float getMaxZoomFactorChange();
}
