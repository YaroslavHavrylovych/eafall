package org.andengine.engine.camera;

/**
 * <p>Smooth camera with fling possibility.</p>
 * <p/>
 * <p>The idea and most of the code is just copied from
 * <a href="https://github.com/davidschreiber/Andengine-Examples/tree/master/FlingCameraExample/AndEngine%20Fling%20Camera/src/at/mmf/andengine/examples/flingcamera">davidschreiber</a>.
 * </p>
 *
 * @author Yaroslav Havrylovych
 */
public class VelocityCamera extends SmoothCamera {
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . C O N S T A N T S
    private static final float DEFAULT_MIN_MOVEMENT_SPEED = 0.01f;
    private static final float DEFAULT_DECELERATION_FACTOR = .9f;


    // . . . . . . . . . . . . . . . . . . . . . . .  P R I V A T E  F I E L D S
    // Movement threshold before stopping the camera
    private float mMinMovementVelocityX;
    private float mMinMovementVelocityY;

    // Actual speed (pixels / second)
    private float mCurrentSpeedX;
    private float mCurrentSpeedY;

    // Deceleration factor each second
    private float mDecelerationFactor;

    // Stops flinging
    private boolean mFlingActivated;


    // . . . . . . . . . . . . . . . . . . . . . . . . . C O N S T R U C T O R S

    public VelocityCamera(final float pX, final float pY, final float pWidth, final float pHeight,
                          final float pMaxVelocityX, final float pMaxVelocityY, final float pMaxZoomFactorChange) {
        super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY, pMaxZoomFactorChange);

        this.mDecelerationFactor = DEFAULT_DECELERATION_FACTOR;
        this.mMinMovementVelocityX = DEFAULT_MIN_MOVEMENT_SPEED;
        this.mMinMovementVelocityY = DEFAULT_MIN_MOVEMENT_SPEED;
    }

    // . . . . . . . . . . . . . . . . . . . . . . .  P U B L I C  M E T H O D
    public float getMinMovementVelocityX() {
        return mMinMovementVelocityX;
    }

    public float getMinMovementVelocityY() {
        return mMinMovementVelocityY;
    }

    public float getDecelerationFactor() {
        return mDecelerationFactor;
    }

    public void setMinMovementVelocityX(float minMovementVelocityX) {
        mMinMovementVelocityX = minMovementVelocityX;
    }

    public void setMinMovementVelocityY(float minMovementVelocityY) {
        mMinMovementVelocityY = minMovementVelocityY;
    }

    public void setDecelerationFactor(float decelerationFactor) {
        mDecelerationFactor = decelerationFactor;
    }

    public void fling(final float pVelocityX, final float pVelocityY) {
        // Set the velocity
        this.mCurrentSpeedX = pVelocityX;
        this.mCurrentSpeedY = pVelocityY;

        // Activate fling
        this.mFlingActivated = true;
    }

    public void stopFling() {
        // Deactivate fling
        this.mFlingActivated = false;
    }


    // . . . . . . . . . . . . . . . . .  S U P E R C L A S S  O V E R R I D E S

    @Override
    public void setCenter(float pCenterX, float pCenterY) {
        // Deactivate Fling
        this.mFlingActivated = false;

        // Set camera center
        super.setCenter(pCenterX, pCenterY);
    }

    @Override
    public void onUpdate(final float pSecondsElapsed) {
        super.onUpdate(pSecondsElapsed);

        // Is fling activated?
        if (mFlingActivated) {
            // Is movement needed?
            if (mCurrentSpeedX != 0 || mCurrentSpeedY != 0) {

                // Calculate deceleration
                this.mCurrentSpeedX *= this.mDecelerationFactor;
                this.mCurrentSpeedY *= this.mDecelerationFactor;

                // Calculate movement
                final float movementX = this.mCurrentSpeedX * pSecondsElapsed;
                final float movementY = this.mCurrentSpeedY * pSecondsElapsed;

                // Stop fling if X and Y velocity is within threshold
                if (Math.abs(mCurrentSpeedX) <= mMinMovementVelocityX
                        && Math.abs(mCurrentSpeedY) <= mMinMovementVelocityY) {
                    mFlingActivated = false;
                    mCurrentSpeedX = 0;
                    mCurrentSpeedY = 0;
                }

                // Calculate camera coordinates
                final float dX = this.getCenterX() - movementX;
                final float dY = this.getCenterY() + movementY;

                // Move camera
                super.setCenter(dX, dY);
            }
        }
    }
}
