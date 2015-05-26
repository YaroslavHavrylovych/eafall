package com.gmail.yaroslavlancelot.eafall.game.camera;

import org.andengine.engine.camera.VelocityCamera;

/**
 * Custom camera for doing some tricks and callbacks
 *
 * @author Yaroslav Havrylovych
 */
public class EaFallCamera extends VelocityCamera {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public EaFallCamera(final float pX, final float pY, final float pWidth, final float pHeight, final float pMaxVelocityX, final float pMaxVelocityY, final float pMaxZoomFactorChange) {
        super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
