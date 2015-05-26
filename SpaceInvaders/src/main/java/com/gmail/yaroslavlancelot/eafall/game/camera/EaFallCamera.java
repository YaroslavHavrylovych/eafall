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
    private ICenterChangedCallback mCenterChangedCallback;

    // ===========================================================
    // Constructors
    // ===========================================================
    public EaFallCamera(final float pX, final float pY, final float pWidth, final float pHeight, final float pMaxVelocityX, final float pMaxVelocityY, final float pMaxZoomFactorChange) {
        super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * set callback on camera center change
     *
     * @param centerChangedCallback callback to be triggered
     */
    public void setCenterChangedCallback(final ICenterChangedCallback centerChangedCallback) {
        mCenterChangedCallback = centerChangedCallback;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void set(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
        super.set(pXMin, pYMin, pXMax, pYMax);
        if (mCenterChangedCallback != null) {
            mCenterChangedCallback.centerChanged(getCenterX(), getCenterY());
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public interface ICenterChangedCallback {
        /**
         * triggers after {@link EaFallCamera#set(float, float, float, float)} operation (as it points
         * when center was changed)
         *
         * @param x camera new x
         * @param y camera new y
         */
        void centerChanged(float x, float y);
    }
}
