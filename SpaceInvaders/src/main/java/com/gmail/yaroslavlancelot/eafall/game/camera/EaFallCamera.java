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
    protected boolean mSmoothZoomInProgress;
    private ICameraMoveCallbacks mCenterChangedCallback;

    // ===========================================================
    // Constructors
    // ===========================================================
    public EaFallCamera(final float pX, final float pY, final float pWidth, final float pHeight,
                        final float pMaxVelocityX, final float pMaxVelocityY,
                        final float pMaxZoomFactorChange) {
        super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * @return true if smooth zoom is currently working and false if it's done
     */
    public boolean isSmoothZoomInProgress() {
        return mSmoothZoomInProgress;
    }

    /**
     * set callback on camera center change
     *
     * @param centerChangedCallback callback to be triggered
     */
    public void setCenterChangedCallback(final ICameraMoveCallbacks centerChangedCallback) {
        mCenterChangedCallback = centerChangedCallback;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void moveCenter(final float pDeltaX, final float pDeltaY) {
        super.moveCenter(pDeltaX, pDeltaY);
        if (mCenterChangedCallback != null) {
            mCenterChangedCallback.cameraMove(pDeltaX, pDeltaY);
        }
    }

    @Override
    protected void onSmoothZoomStarted() {
        super.onSmoothZoomStarted();
        mSmoothZoomInProgress = true;
    }

    @Override
    protected void onSmoothZoomFinished() {
        super.onSmoothZoomFinished();
        mSmoothZoomInProgress = false;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public interface ICameraMoveCallbacks {

        /**
         * triggers after {@link EaFallCamera#moveCenter(float, float)}
         *
         * @param deltaX x increasing value
         * @param deltaY y increasing value
         */
        void cameraMove(float deltaX, float deltaY);
    }
}
