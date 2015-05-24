package org.andengine.input.touch.detector;

import android.view.MotionEvent;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @author Yarolav Havrylovych
 */
public class PinchZoomDetector extends BaseDetector {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final float TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT = 10;

    // ===========================================================
    // Fields
    // ===========================================================

    private final IPinchZoomDetectorListener mPinchZoomDetectorListener;

    private float mInitialDistance;
    private float mCurrentDistance;
    private float mTriggerPinchZoomMinimumDistance = TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT;

    private boolean mPinchZooming;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PinchZoomDetector(final IPinchZoomDetectorListener pPinchZoomDetectorListener) {
        this.mPinchZoomDetectorListener = pPinchZoomDetectorListener;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public boolean isZooming() {
        return this.mPinchZooming;
    }

    public void setTriggerPinchZoomMinimumDistance(float triggerPinchZoomMinimumDistance) {
        this.mTriggerPinchZoomMinimumDistance = triggerPinchZoomMinimumDistance;
    }

    public float getTriggerPinchZoomMinimumDistance() {
        return this.mTriggerPinchZoomMinimumDistance;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    /**
     * When {@link #isZooming()} this method will call through to {@link IPinchZoomDetectorListener#onPinchZoomFinished(PinchZoomDetector, TouchEvent, float)}.
     */
    @Override
    public void reset() {
        if (this.mPinchZooming) {
            this.mPinchZoomDetectorListener.onPinchZoomFinished(this, null, this.getZoomFactor());
        }

        this.mInitialDistance = 0;
        this.mCurrentDistance = 0;
        this.mPinchZooming = false;
    }

    @Override
    public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
        final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

        final int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!this.mPinchZooming && PinchZoomDetector.hasTwoPointers(motionEvent)) {
                    this.mInitialDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
                    this.mCurrentDistance = this.mInitialDistance;
                    if (this.mInitialDistance > this.mTriggerPinchZoomMinimumDistance) {
                        this.mPinchZooming = true;
                        this.mPinchZoomDetectorListener.onPinchZoomStarted(this, pSceneTouchEvent);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (this.mPinchZooming) {
                    this.mPinchZooming = false;
                    this.mPinchZoomDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, this.getZoomFactor());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.mPinchZooming) {
                    if (PinchZoomDetector.hasTwoPointers(motionEvent)) {
                        this.mCurrentDistance = PinchZoomDetector.calculatePointerDistance(motionEvent);
                        if (this.mCurrentDistance > this.mTriggerPinchZoomMinimumDistance) {
                            this.mPinchZoomDetectorListener.onPinchZoom(this, pSceneTouchEvent, this.getZoomFactor());
                        }
                    } else {
                        this.mPinchZooming = false;
                        this.mPinchZoomDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, this.getZoomFactor());
                    }
                }
                break;
        }
        return true;
    }

    /**
     * check is there only two pointers (touches) present currently.
     * <br/>
     * one used for scroll
     * <br/>
     * more than two can be used for some other thing
     */
    private static boolean hasTwoPointers(final MotionEvent pMotionEvent) {
        return pMotionEvent.getPointerCount() == 2;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Calculate the euclidian distance between the first two fingers.
     */
    private static float calculatePointerDistance(final MotionEvent pMotionEvent) {
        return MathUtils.distance(pMotionEvent.getX(0), pMotionEvent.getY(0), pMotionEvent.getX(1), pMotionEvent.getY(1));
    }

    private float getZoomFactor() {
        return this.mCurrentDistance / this.mInitialDistance;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface IPinchZoomDetectorListener {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pSceneTouchEvent);

        void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor);

        void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor);
    }
}
